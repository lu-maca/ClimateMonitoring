package uni.climatemonitor;

import uni.climatemonitor.common.ClimateParameter;
import uni.climatemonitor.common.IClient;
import uni.climatemonitor.graphics.UtilsSingleton;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IClient {

    public Client() throws RemoteException {}

    @Override
    public void updateMe(ClimateParameter climateParameter) throws RemoteException {
        UtilsSingleton utils = UtilsSingleton.getInstance();
        utils.getDetailsPnl().updateDate(climateParameter);
    }
}
