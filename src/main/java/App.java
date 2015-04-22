import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

/**
 * Created by petrpopov on 22.04.15.
 */
public class App {

    private static String defaultPort = "/dev/ttyUSB0";
    private static Integer defaultID = 18;

    /**
     * Using first param PORT then UNITID
     * java -jar program.jar /dev/ttyUSB0 18
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        System.out.println("Hello. Testing Modbus with JSSC");

        String port = defaultPort;
        Integer unitID = defaultID;

        if(args.length >= 1) {
            port = args[0];
            System.out.println("Using port: " + port);
        }
        else {
            System.out.println("Using default port: " + defaultPort);
        }

        if(args.length >= 2) {
            unitID = Integer.parseInt(args[1]);
            System.out.println("Using UNITID: " + unitID);
        }
        else {
            System.out.println("Using default UNITID: " + unitID);
        }

        ModbusCoupler.getReference().setUnitID(unitID);

        //3. Setup serial parameters
        SerialParameters params = new SerialParameters();

        params.setPortName(port);
        params.setBaudRate(9600);
        params.setDatabits(8);
        params.setParity("None");
        params.setStopbits(1);
        params.setEncoding("rtu");
        params.setEcho(false);
        params.setReceiveTimeout(100);

        System.out.println("Opening connection on port: " + port);
        SerialConnection connection = new SerialConnection(params);
        connection.open();

        System.out.println("Preparing read 4 register on unitid: " + unitID);
        ReadInputRegistersRequest request = new ReadInputRegistersRequest(0, 4);
        request.setUnitID(unitID);
        request.setHeadless();

        System.out.println("Opening transaction");
        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);
        transaction.setRequest(request);

        System.out.println("Executing transaction");
        transaction.execute();

        System.out.println("Reading response");
        ReadInputRegistersResponse result = (ReadInputRegistersResponse) transaction.getResponse();
        for (int i = 0; i < result.getWordCount(); i++) {
            System.out.println(result.getRegisterValue(i));
        }

        System.out.println("Closing connection");

        connection.close();
//        params.setPortName("/dev/ttyUSB1");
//        connection = new SerialConnection(params);
//        connection.open();

        System.out.println("End of testing modbus");
    }
}
