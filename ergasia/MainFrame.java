package ergasia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import ithakimodem.Modem;

public class MainFrame extends JFrame {
	AnimatedGraph ag;
	JPanel AnimatedPanel;
	JTextArea BitErrorRate;
	JButton echo ;
	JButton img ;
	JButton errImg ;
	JButton gps ;
	JButton err ;
	JButton gpsImg;
	final JTextField code;
	final JTextField code2;
	final JTextField repetition;
	JTextArea desc;
	JTextArea descRep;
	JTextArea descNack;
	JTextArea console;
	JScrollPane scroll;
	ImageWindow text;
	JProgressBar bar;
	JTextArea loading;
	JTextArea ping;
	volatile int function = 0;
	boolean printGpsImage = false;
	public MainFrame() throws Exception {
		MainFrame f = this;
		f.setLayout(null);
		this.setSize(1000,730);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		AnimatedPanel = new JPanel();
		BitErrorRate = new JTextArea();
		ping = new JTextArea();
		repetition = new JTextField();
		text = new ImageWindow();
		echo = new JButton("ECHO");
		img = new JButton("IMAGE");
		errImg = new JButton("IMAGEwERROR");
		gps = new JButton("GPS");
		err = new JButton("ACK/NACK");
		code = new JTextField();
		code2 = new JTextField();
		descNack = new JTextArea();
		descRep = new JTextArea();
		desc = new JTextArea("Enter your code \nin the field above ,then\npress the button\nwhich corresponds to\nthe action you want to\ntake");
		console = new JTextArea(50,200);
		scroll = new JScrollPane(console);
		bar = new JProgressBar();
		gpsImg = new JButton("Print Image");
		loading = new JTextArea(20, 1);
		bar.setValue(0);
		bar.setStringPainted(true);
		text.setBounds(f.getWidth()/100 + f.getWidth()/7, f.getHeight()/100 + 5*f.getHeight()/20,f.getWidth() - f.getWidth()/7 - f.getWidth()/90, f.getHeight()-5*f.getHeight()/20- f.getHeight()/10);
		text.setLayout(new FlowLayout());
		
		//scroll.setBounds(f.getWidth()/100 + f.getWidth()/7, f.getHeight()/100 + 5*f.getHeight()/20,f.getWidth() - f.getWidth()/7 - f.getWidth()/90, f.getHeight()-5*f.getHeight()/20- f.getHeight()/10);
		//console.setBounds(f.getWidth()/100 + f.getWidth()/7, f.getHeight()/100 + 5*f.getHeight()/20, f.getWidth() - f.getWidth()/7 - f.getWidth()/38, f.getHeight()-5*f.getHeight()/20- f.getHeight()/10 );
		BitErrorRate.setBounds(f.getWidth()/100, f.getHeight()-f.getHeight()/100-f.getHeight()/10-f.getHeight()/20, f.getWidth()/7, f.getHeight()/20);
		ping.setBounds(f.getWidth()/100, f.getHeight()-f.getHeight()/100-f.getHeight()/10, f.getWidth()/7, f.getHeight()/20);
		echo.setBounds(f.getWidth()/100, f.getHeight()/100, f.getWidth()/7, f.getHeight()/20);
		img.setBounds(f.getWidth()/100, f.getHeight()/100+f.getHeight()/20, f.getWidth()/7, f.getHeight()/20);
		errImg.setBounds(f.getWidth()/100, f.getHeight()/100+2*f.getHeight()/20, f.getWidth()/7, f.getHeight()/20);
		gps.setBounds(f.getWidth()/100, f.getHeight()/100+3*f.getHeight()/20, f.getWidth()/7, f.getHeight()/20);
		err.setBounds(f.getWidth()/100, f.getHeight()/100+4*f.getHeight()/20, f.getWidth()/7, f.getHeight()/20);
		code.setBounds(f.getWidth()/100 +f.getWidth()/7 ,f.getHeight()/100 ,f.getWidth()/7, f.getHeight()/20 );
		code2.setBounds(f.getWidth()/100 +f.getWidth()/7+f.getWidth()/7,f.getHeight()/100,f.getWidth()/7, f.getHeight()/20 );
		repetition.setBounds(f.getWidth()/100 +f.getWidth()/7+f.getWidth()/7,f.getHeight()/100,f.getWidth()/7, f.getHeight()/20 );
		descRep.setBounds(f.getWidth()/100 +f.getWidth()/7+f.getWidth()/7,f.getHeight()/100+f.getHeight()/20,f.getWidth()/7, 4*f.getHeight()/20 );
		descNack.setBounds(f.getWidth()/100 +f.getWidth()/7+f.getWidth()/7,f.getHeight()/100+f.getHeight()/20,f.getWidth()/7, 4*f.getHeight()/20 );
		desc.setBounds(f.getWidth()/100 +f.getWidth()/7,f.getHeight()/100 +f.getHeight()/20, f.getWidth()/7, 4*f.getHeight()/20  );
		desc.setOpaque(false);
		bar.setBounds(f.getWidth()/100 , f.getHeight()/100+6*f.getHeight()/20, f.getWidth()/7, f.getHeight()/20 );
		loading.setBounds(f.getWidth()/100, f.getHeight()/100+7*f.getHeight()/20, f.getWidth()/7, f.getHeight()/20 );
		gpsImg.setBounds(f.getWidth()/100, f.getHeight()/100+8*f.getHeight()/20, f.getWidth()/7, f.getHeight()/20 );
		AnimatedPanel.setBounds(f.getWidth()/100 + 4*f.getWidth()/7, f.getHeight()/100, f.getWidth() - f.getWidth()/100 - 4*f.getWidth()/7  , f.getHeight()/4 );
		AnimatedPanel.setLayout(new FlowLayout());
		ag = new AnimatedGraph(0,0,AnimatedPanel.getWidth(), AnimatedPanel.getHeight());
		descNack.setOpaque(false);
		descNack.setLineWrap(true);
		descNack.setWrapStyleWord(true);
		descNack.setEditable(false);
		descNack.setVisible(false);
		descNack.setText("Enter the NACK code here");
		descRep.setOpaque(false);
		descRep.setLineWrap(true);
		descRep.setWrapStyleWord(true);
		descRep.setEditable(false);
		descRep.setText("Enter how many times you want to execute the same action.");
		desc.setLineWrap(true);
		desc.setWrapStyleWord(true);
		desc.setEditable(false);
		console.setOpaque(true);
		console.setEditable(false);
		console.setBackground(getBackground());
		console.setForeground(Color.black);
		loading.setOpaque(false);
		loading.setText("Loading Image");
		loading.setVisible(false);
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		gpsImg.setVisible(false);
		ping.setOpaque(false);
		ping.setText("Delay: Non" );
		BitErrorRate.setText("Bit Error Rate:  Non");
		BitErrorRate.setOpaque(false);
		BitErrorRate.setVisible(false);
		//console.setMinimumSize(new Dimension(f.getWidth() - f.getWidth()/7 - f.getWidth()/38, f.getHeight()-5*f.getHeight()/20- f.getHeight()/16 ));
		scroll.setPreferredSize(new Dimension(f.getWidth() - f.getWidth()/7 - f.getWidth()/90, f.getHeight()-5*f.getHeight()/20- f.getHeight()/9));
		
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		scroll.setBorder(BorderFactory.createEmptyBorder());
		code2.setVisible(false);
		echo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				gpsImg.setVisible(false);
				Tools.stringRes = "";
				final String c = (String)code.getText() + "\r";
				
				
				if(c.equals("")||c.equals("Code")) {
					
				}else  {
					new Thread() {
						public void run() {
						Modem modem = Tools.connectToIth();
						function = 1;
						Tools.echoread(modem, c);
						modem.close();
						}
					}.start();
				}
			}
		});
		img.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				gpsImg.setVisible(false);
				Tools.stringRes = "";
			final String c = (String)code.getText()+"\r";
			final int rep = textFieldToInteger(repetition.getText());
				
				if(c.equals("")||c.equals("Code")) {
					System.out.println("Please enter the code in the field");
				}else  {
					new Thread() {
						public void run() {
						Modem modem = Tools.connectToIth();
						function = 2;
						try {
							for(int i=0; i<rep; i++) {
								bar.setVisible(true);
								loading.setVisible(true);
								if(c.contains("F")||c.length()<8)
									Tools.imageread(modem,  c,85000);
								else
									Tools.imageread(modem,  c,60000);
								bar.setVisible(false);
								loading.setVisible(false);
								bar.setValue(0);
							}
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						modem.close();
						}
					}.start();
				}
			}
		});
		errImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				gpsImg.setVisible(false);
				Tools.stringRes = "";
			final String c = (String)code.getText()+"\r";
			final int rep = textFieldToInteger(repetition.getText());
				
				if(c.equals("")||c.equals("Code")) {
					System.out.println("Please enter the code in the field");
				}else  {
					new Thread() {
						public void run() {
						Modem modem = Tools.connectToIth();
						function = 2;
						try {
							for(int i=0; i<rep; i++) {
								bar.setVisible(true);
								loading.setVisible(true);
								if(c.contains("F")||c.length()<8)
									Tools.imageread(modem,  c,75000);
								else
									Tools.imageread(modem,  c,55000);
								bar.setVisible(false);
								loading.setVisible(false);
								bar.setValue(0);
							}
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						modem.close();
						}
					}.start();
				}
			}
		});
		gps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				gpsImg.setVisible(false);
				Tools.stringRes = "";
			final String c = (String)code.getText();
			final int rep = textFieldToInteger(repetition.getText());
				
				if(c.equals("")||c.equals("Code")) {
					System.out.println("Please enter the code in the field");
				}else  {
					new Thread() {
						public void run() {
						Modem modem = Tools.connectToIth();
						function = 4;
						
						try {
							for(int i=0; i<rep; i++) {
								bar.setVisible(true);
								loading.setVisible(true);
								Tools.gpsread(modem, c);
								gpsImg.setVisible(true);
								bar.setVisible(false);
								loading.setVisible(false);
							}
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						modem.close();
						}
					}.start();
				}
			}
		});
		err.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				gpsImg.setVisible(false);
				Tools.stringRes = "";
			final String c = (String)code.getText()+"\r";
			final String c2 = (String)code2.getText() + "\r";
			
				
				if(code2.isVisible()) {
					if(c.equals("")||c.equals("Code")) {
						System.out.println("Please enter the code in the field");
					}else  {
						new Thread() {
							public void run() {
							BitErrorRate.setVisible(true);
							final int rep = textFieldToInteger(repetition.getText());
							Modem modem = Tools.connectToIth();
							function = 5;
							try {
								for(int i=0; i<rep; i++) {
									Tools.erRead(modem, c, c2, i);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							BitErrorRate.setVisible(false);
							code2.setVisible(false);
							descNack.setVisible(false);
							repetition.setBounds(f.getWidth()/100 +f.getWidth()/7+f.getWidth()/7,f.getHeight()/100,f.getWidth()/7, f.getHeight()/20 );
							descRep.setBounds(f.getWidth()/100 +f.getWidth()/7+f.getWidth()/7,f.getHeight()/100+f.getHeight()/20,f.getWidth()/7, 4*f.getHeight()/20 );
							modem.close();
							}
						}.start();
					}
				}else {
					repetition.setBounds(f.getWidth()/100 +2*f.getWidth()/7+f.getWidth()/7,f.getHeight()/100,f.getWidth()/7, f.getHeight()/20 );
					descRep.setBounds(f.getWidth()/100 +2*f.getWidth()/7+f.getWidth()/7,f.getHeight()/100+f.getHeight()/20,f.getWidth()/7, 4*f.getHeight()/20 );
					descNack.setVisible(true);
					code2.setVisible(true);
				}
			}
		});
		gpsImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				function=2;
				Tools.stringRes = "";
				printGpsImage = true;
				bar.setValue(0);
				gpsImg.setVisible(false);
				}
			
		});
		
		TimerTask updater  = new TimerTask() {
			public void run() {
				if (function==1) {
					scroll.setVisible(true);
					text.setImage(null);
					text.revalidate();
					text.repaint();
					
					
					
					
					if (Tools.updated)
						console.append(Tools.stringRes);
					Tools.updated = false;
					
				}else if(function ==2) {
					scroll.setVisible(false);
					
					if(Tools.percentage>0) {
						
						updateBar(Tools.percentage);
					}
					Tools.percentage = 0;
					text.setImage(Tools.imageRes);
					text.repaint();
				}else if(function == 4) {
					scroll.setVisible(true);
					text.setImage(null);
					text.revalidate();
					text.repaint();
					if (Tools.percentage>0) {
						updateBar(Tools.percentage);
					}
					Tools.percentage = 0;
					if (Tools.updated)
					console.append(Tools.stringRes);
					
					Tools.updated = false;
				}else if(function == 5) {
					scroll.setVisible(true);
					text.setImage(null);
					text.revalidate();
					text.repaint();
					if(Tools.BERupdated) {
						DecimalFormat df = new DecimalFormat();
						df.setMaximumFractionDigits(2);
						BitErrorRate.setText("Bit Error Rate: " + df.format(Tools.bitErrorRate*100) + "%");
						try {
							Tools.saveString(df.format(Tools.bitErrorRate*100), "ber.txt");
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						Tools.BERupdated=false;
					}
					if (Tools.updated)
						console.append(Tools.stringRes+"\n");
					Tools.updated = false;
				}
			}
		};
		Timer reload = new Timer("reloader", false );
		reload.scheduleAtFixedRate(updater, 30 , 100);
		 
		TimerTask pingUpdate = new TimerTask() {
			public void run() {
				if(Tools.pingUpdated) {
					try {
						
						Tools.saveString(Integer.toString((int) Tools.ping), "ping.txt");
						ping.setText("Delay: " + Integer.toString((int) Tools.ping));
						ag.points.add((int)(Tools.ping));
						ag.movePoints();
						Tools.ping = 0;
						Tools.pingUpdated = false;
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		};
		Timer pUpdater = new Timer("pingUpdater", false);
		pUpdater.scheduleAtFixedRate(pingUpdate, 30, 5);
		//f.add(console);
		//scroll.add(console);
	
		bar.setVisible(false);
		text.add(scroll);
		AnimatedPanel.add(ag);
		f.add(BitErrorRate);
		f.add(AnimatedPanel);
		f.add(ping);
		f.add(descNack);
		f.add(descRep);
		f.add(repetition);
		f.add(bar);
		f.add(text);
		f.add(echo);
		f.add(img);
		f.add(errImg);
		f.add(gps);
		f.add(err);
		f.add(code);
		f.add(code2);
		f.add(desc);
		f.add(loading);
		f.add(gpsImg);
		f.setLayout(new BorderLayout());
		
		this.setVisible(true); 
	}
	public void updateBar(int percentage) {
		
		bar.setValue(bar.getValue()+5*percentage);
	}
	public int textFieldToInteger(String text) {
		if (text.length()==0) {
			return 1;
		}else
			return Integer.parseInt(text);
	}
}
