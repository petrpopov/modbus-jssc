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



    public static void main(String[] args) throws Exception {

        System.out.println("Hello. Testing Modbus with JSSC");

        ModbusCoupler.getReference().setUnitID(1);

        //3. Setup serial parameters
        SerialParameters params = new SerialParameters();

        params.setPortName("/dev/ttyUSB0");
        params.setBaudRate(9600);
        params.setDatabits(8);
        params.setParity("None");
        params.setStopbits(1);
        params.setEncoding("rtu");
        params.setEcho(false);
        params.setReceiveTimeout(100);

        SerialConnection connection = new SerialConnection(params);
        connection.open();

        ReadInputRegistersRequest request = new ReadInputRegistersRequest(0, 4);
        request.setUnitID(1);
        request.setHeadless();

        ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);
        transaction.setRequest(request);
        transaction.execute();

        ReadInputRegistersResponse result = (ReadInputRegistersResponse) transaction.getResponse();
        for (int i = 0; i < result.getWordCount(); i++) {
            System.out.println(result.getRegisterValue(i));
        }

        System.out.println("End of testing modbus");
    }
}
