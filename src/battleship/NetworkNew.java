package battleship;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class NetworkNew {
	
	
	private String ip_and_host = "http://23.239.16.47:7777";
	public static Socket socket;
	private SelectShip myShipBoard;
	
	public NetworkNew() {
		
		// TODO Auto-generated constructor stub
				
				
				
				System.out.println("Creating a new connection....");
				
				URI uri = URI.create(ip_and_host);
				IO.Options options = IO.Options.builder()
				        // ...
				        .build();

				NetworkNew.socket = IO.socket(uri, options);
				
				myShipBoard = new SelectShip(null);
				
				NetworkNew.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				    @Override
				    public void call(Object... args) {
				    	System.out.println("I sucess connected");
				    	NetworkNew.socket.emit("username", "me");
				    	System.out.println("joined room...");
				    	
				    }
				}).on("from flask", new Emitter.Listener() {
					
					@Override
					public void call(Object... arg0) {
						String myString = arg0[0].toString();
						System.out.println(myString);
					}
				}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
					
					@Override
					public void call(Object... arg0) {
						System.out.println("Im disconnected!");
						
					}
				}).on("game_ready", new Emitter.Listener() { //when game is ready from the other player
					
					@Override
					public void call(Object... arg0) {
						System.out.println("games ready... ");
						myShipBoard.setReady(true, arg0[0].toString());	
					}
						
				}).on("send_board", new Emitter.Listener() { //when a player want to send its board to other players 
					
					@Override
					public void call(Object... arg0) {
						
						//JSONObject jsonOBJ = new JSONObject(arg0[0].toString());
						myShipBoard.processBoard(arg0[0].toString());
						NetworkNew.socket.emit("finished_process_board", "I finished processing board");
						
						
					}
						
				}).on("recieveAttack", new Emitter.Listener() { //when a player want to send its board to other players 
					
					@Override
					public void call(Object... arg0) {
						
						try {
							JSONObject jsonOBJ = new JSONObject(arg0[0].toString());
							myShipBoard.getFinalGui().recieveAttackMove(jsonOBJ);
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
						
				}).on("roommatesLeft", new Emitter.Listener() {
					
					@Override
					public void call(Object... arg0) {
						myShipBoard.getFinalGui().disconnect(arg0[0].toString());
						
					}
				});
				
				NetworkNew.socket.connect();
				
			}
	
		
	}
