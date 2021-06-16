package t1;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DIsplayFrame {
	
	private static JFrame frame;
	private static JPanel panel;
	private static String getComport;
	private static JTextField JT_comPort;
	private static JButton JB_submitComport , JB_beginButton;
	private static JLabel JL_comPort;
	public static JTextArea display_signal;
	public static int signal = 0;
	
	/*setBounds(int x, int y,int width, int height)*/
	
	public static void main(String[] args) {	
		initFrame();
	}
	
	public static void printSignal(String str) {
		/*自動將焦點滾動至最新的內容*/
		display_signal.append(str);
		display_signal.setCaretPosition(display_signal.getText().length());
	}
	
	private static void initFrame() {
		frame = new JFrame("Arduino game");
		frame.setSize(420,450);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initPane();
		frame.setContentPane(panel);		
	}
	
	private static void initLabel() {
		JL_comPort = new JLabel("Setting com port:");
		JL_comPort.setBounds(5,5,100,50);
		panel.add(JL_comPort);	
	}
	
	private static void initPane() {
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0,0,400,400);
		panel.setBackground(Color.GREEN);	
		initButton();
		initLabel();
		initTextField();
		initDisplaySignalTextField();
		initBeginButton();
	}
	
	private static void initDisplaySignalTextField() {
		display_signal = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(display_signal);
		scrollPane.setBounds(0,90,400,250);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		display_signal.setBounds(0,0,400,300);
		panel.add(scrollPane);
	}
	
	private static void initBeginButton() {
		JB_beginButton = new JButton("開始遊戲");
		JB_beginButton.setBounds(0,345,400,30);
		panel.add(JB_beginButton);
		
		JB_beginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					new TwoWaySerialComm.GameFrame();
				} catch (InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.disable();
			}
			
		});
	}
	
	private static void initButton() {
		JButton JB_submitComport = new JButton("確認");
		JB_submitComport.setBounds(5,45,100,30);
		panel.add(JB_submitComport);
		
		/*To do print data to TextArea from Arduino Board.*/
		JB_submitComport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getComport = JT_comPort.getText();
				TwoWaySerialComm twsc = new TwoWaySerialComm();
				try {
					new TwoWaySerialComm.GameFrame();
				} catch (InterruptedException | IOException e1) {
					e1.printStackTrace();
				}
				try {
					twsc.connect(getComPort());
				} catch (Exception e) {e.printStackTrace();}
			}
		});
	}
	
	public static JPanel getPanel() {
		return DIsplayFrame.panel;
	}
	
	private static String getComPort() {
		return JT_comPort.getText();
	}
	
	private static void initTextField() {
		JT_comPort = new JTextField("COM");
		JT_comPort.setBounds(110,15,50,30);
		panel.add(JT_comPort);
	}	
}
