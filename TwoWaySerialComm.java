package t1;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
public class TwoWaySerialComm {

    public TwoWaySerialComm()
    {
        super();
    }
     
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
             
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                 
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);
                (new Thread(new SerialWriter(out))).start();
            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }

    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
            int count = 0;
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                String s = new String(buffer,0,len);
                /*String[] s_arr = s.split("\n");
                for(String v : s_arr) {
                	System.out.print(++count+"@@@"+v);
                }*/
                System.out.println(s);
                //System.out.println(s.substring(0, s.length()-1));
                //System.out.print(s.substring(0,s.indexOf("\n")));
                DIsplayFrame.printSignal(s);
                GameFrame.getSignalAction(s,0);
                Thread.sleep(300);
            }
            catch ( IOException  | InterruptedException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        
        public String avgOfSignal(String value) {
        	String[] value_arr ;
        	int total_value = 0;
        	int count = 0;
        	//try {
        	value_arr = value.split("\n");
        	for(String v : value_arr) {
        		total_value += Integer.parseInt(v);
        		System.out.println(++count+"@@@"+v);
        	}
        	//}catch(NumberFormatException ne) {}
        	
        	return String.valueOf(total_value);
        }
    }
    public static class SerialWriter implements Runnable 
    {
        OutputStream out ;
         
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
         
        public void run ()
        {
            try
            {
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
     
    public static void main ( String[] args ){}
    
    public static class GameFrame extends JFrame implements KeyListener {
    	
    	private static Image img = null;
    	private static Graphics grp;
    	private Container content;
    	private static JPanel p;
    	final static int INIT_X_POS = 250, INIT_Y_POS = 100; //圖片初始位址
    	//X移動, Y移動, 圖片X, 圖片Y, 圖片寬度, 圖片高度
    	static int SpeedX = 5, SpeedY = 5, imgX = INIT_X_POS,
    			imgY = INIT_Y_POS, imgW = 100, imgH = 100;
    	static String[] s ;
    	public GameFrame() throws InterruptedException, IOException {
    		super("Game Frame");
    		content = this.getContentPane();
    		this.setBounds(350, 200, 650, 350);
    		this.setResizable(false);
    		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    		initPanel();
    		this.setVisible(true);
    		paintImage();
    		this.addKeyListener(this);
    	}
    	
    	private void paintImage() throws IOException, InterruptedException{

    		img = Toolkit.getDefaultToolkit().getImage("D:\\下載\\fish.jpg");
    		grp = p.getGraphics();
    		MediaTracker t = new MediaTracker(this);
            t.addImage(img, 0);
            t.waitForAll();
            grp.drawImage(img, imgX, imgY, imgW, imgH, null);
    	}
    		
    	private void initPanel() {
    		p = new JPanel();
    		p.setLayout(new BorderLayout());
    		JLabel background=new JLabel(new ImageIcon("D:\\下載\\background.jpg"));
    		p.add(background);
    		content.add(p,BorderLayout.CENTER);
    	}
    	
    	public static void getSignalAction(String s , int count)throws NumberFormatException {

			grp.drawImage(img, imgX, imgY, imgW, imgH, null);
			grp.clearRect(imgX, imgY, imgW, imgH);
			while(count < 1){
				count++;
				/*Need to modify parameters , shall greater than 490*/
				if((imgY - SpeedY >= 0 || imgY + imgH + SpeedY <= 350)) {
					for(int i=0 ; i<1 ; i++) {
						//System.out.println(i+"@@@@"+s);
					if(s.substring(0,s.length()-1).compareTo("490") > 0) {
						imgY -= ((imgY - SpeedY) >= 0 ? SpeedY : imgY);
					}else {
						imgY += ((imgY + imgH + SpeedY) <= 350 ? SpeedY : 350 - imgY - imgH);	
					}
					grp.drawImage(img, imgX, imgY, imgW, imgH, null);
				}
			}
				if(imgY - SpeedY == 0) 
					System.out.println("WIN");
			}
    }
    	
    	public void keyPressed(KeyEvent e) {
    		grp.drawImage(img, imgX, imgY, imgW, imgH, null);
    		grp.clearRect(imgX, imgY, imgW, imgH);
    		int key = e.getKeyCode();
    		//有作邊界判定
    		if(key == KeyEvent.VK_LEFT) { //如(圖片的X位置-移動位置數)小於0　則等同於超出邊界
    			imgX -= ((imgX - SpeedX) >= 0 ? SpeedX : imgX);}
    		if(key == KeyEvent.VK_RIGHT) { //如(圖片的X位置+圖片寬度+移動位置數)大於視窗寬度　則等同於超出邊界
    			imgX += ((imgX + imgW + SpeedX) <= this.getWidth() ? SpeedX : (this.getWidth() - imgX - imgW));}
    		if(key == KeyEvent.VK_UP) {
    			imgY -= ((imgY - SpeedY) >= 0 ? SpeedY : imgY);}
    		if(key == KeyEvent.VK_DOWN) {
    			System.out.println("img height"+img.getHeight(null));
    			if(this.getHeight() - imgY - imgH == 0)
    				imgY += 0;
    			else
    				imgY += ((imgY + imgH + SpeedY) <= this.getHeight() ? SpeedY : this.getHeight() - imgY - imgH);	
    		}
    		
			if(imgY - SpeedY == 0) {
				p.setForeground(Color.BLUE);
			}
//			if(imgY + imgH + SpeedY <= 350)
//				System.out.println("WIW");
    		
    		
    		//(this.getHeight() - imgY - imgH)
    		//沒有作邊界判定
    		if(key == KeyEvent.VK_A) {imgX -= SpeedX;}
    		if(key == KeyEvent.VK_D) {imgX += SpeedX;}
    		if(key == KeyEvent.VK_W) {imgY -= SpeedY;}
    		if(key == KeyEvent.VK_S) {imgY += SpeedY;}
    		
    		grp.drawImage(img, imgX, imgY, imgW, imgH, null);
    		
    	}
    	public void keyReleased(KeyEvent arg0) {}
    	public void keyTyped(KeyEvent arg0) {}
    }
}
