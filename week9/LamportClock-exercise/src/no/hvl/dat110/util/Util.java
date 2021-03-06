package no.hvl.dat110.util;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import no.hvl.dat110.process.Message;
import no.hvl.dat110.process.Config;
import no.hvl.dat110.process.iface.OperationType;
import no.hvl.dat110.process.iface.ProcessInterface;


public class Util {
	
	
	public static Registry locateRegistry() {

		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(Config.PORT);			
			registry.list();
		} catch (RemoteException e) {
			registry = null;
		}
		
		return registry;
	}
	
	public static ProcessInterface registryHandle(String stubID) throws AccessException, RemoteException, NotBoundException {
		
		ProcessInterface process = null;
		
		Registry registry = locateRegistry();
		
		process = (ProcessInterface) registry.lookup(stubID);
		
		return process;
		
	}
	
	public static List<String> getProcessReplicas() {
		List<String> replicas = new ArrayList<String>();
		// assume we have 3 replicas
		replicas.add("process1");
		replicas.add("process2");
		replicas.add("process3");
		
		return replicas;
	}
	
	public static void printClock(ProcessInterface process) throws RemoteException {
		System.out.println("Queue info at process: "+process.getProcessID());
		System.out.println("===============================");
		List<Message> messages = process.getQueue();
		for(int i=0; i<messages.size(); i++) {
			Message message = messages.get(i);
			int counter = message.getClock();
			int procId = message.getProcessID();
			OperationType opt = message.getOptype();
			boolean ack = message.isAcknowledged();
			
			System.out.println(opt.toString()+":"+counter+":"+ack+":"+procId);
		}
		
		System.out.println("===============================");
		System.out.println("Balance: "+process.getBalance());
		System.out.println("===============================");
		System.out.println("");
	}

}
