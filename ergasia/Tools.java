package ergasia;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import ithakimodem.Modem;

public class Tools {
	public volatile static String stringRes;
	public volatile static Image imageRes;
	public volatile static boolean updated;
	public volatile static int percentage; 
	public volatile static long ping;
	public volatile static boolean pingUpdated;
	public volatile static double bitErrorRate;
	public volatile static boolean BERupdated;
	private static int numberOfBits =0;
	private static int wrongBits = 0;
	private static int TimesCalledAck = 0;
	private static int TimesCalledNack = 0;
	public static Modem connectToIth() {
		Modem modem;
		modem=new Modem();
		modem.setSpeed(800000*8);
		modem.setTimeout(1000);
		modem.open("ithaki");
		return modem;
	}
	public static byte[] cleanArray(int[] array) {
		int c = 0;
		int first = 0;
		
		for (int i = 0; i<array.length; i++) {
			if ((byte)array[i]==(byte)0xFF) {
				first = i;
			}
			if((byte)array[i] == (byte)0xD8) {
				if (first == i-1) {
					break;
				}else {
					first = 0;
				
				}
			}
		}
		
		for (int i=first; i<array.length; i++) {
			if (array[i]!=-1000) {
				c++;
			}
		}
		byte[] result = new byte[c ];
	
		for (int i=0; i<c; i++) {
			result[i] = (byte)array[i + first];
		}
		
		return result;
	}
	public static void send(String mes, Modem modem) {
	
		byte[] message = mes.getBytes();
		modem.write(message);
	}
	public static void echoread(Modem modem, String code) {
		int k;
		
		
		String res = "";
		for(;;) {
			long start = System.currentTimeMillis();
			send(code,modem);
			
			int a = 0;
			for (;;) {
				try {
				k=modem.read();
				if (a==0) {
					long end = System.currentTimeMillis();
					ping = end - start;
					pingUpdated = true;
				}
				a++;
				if (k==-1) break;
				
				res+=String.valueOf((char)k);
				} catch (Exception x) {
				break;
				}
			}
			stringRes = res+"\n";
			updated = true;
			res = "";
			
		}
	}
	
	
	public static void imageread(Modem modem, String code, int bytes) throws IOException {
		int[] buf = new int[bytes];
		Arrays.fill(buf , -1000);
	
		int k;
		long start = System.currentTimeMillis();
		send(code, modem);
		int o= 0;
		int a = 0;
		for (;;) {
			try {
			
			k=modem.read();
			if (o==0) {
				long end = System.currentTimeMillis();
				ping = end - start;
				pingUpdated = true;
			}
			if (k==-1) break;
			
			
			buf[o] = k;
			
			o++;
			a++;
			if (o%1000 ==0) {
				
				if((int)((float)a/(float)bytes*(float)100)>=5) {
					a = 0;
					percentage = 1;
					
				}else if((int)((float)a/(float)bytes*(float)100)>=10) {
					a=0;
					percentage = 2;
				}else if((int)((float)a/(float)bytes*(float)100)>=15) {
					a=0;
					percentage = 3;
				}
				
				
			} 
			
			//result += (char)k;
			} catch (Exception x) {
				
			break;
			}
		}
		
	
		byte[] byteArray = cleanArray(buf);
	
		
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
		BufferedImage bImage2 = ImageIO.read(bis);
		imageRes = bImage2;
		ImageIO.write(bImage2, "jpeg", new File("output.jpeg") );
		
		
		
	
	}
	
	
public static String[] cleanGps(String message) {
		
		String spattern = "GPGGA,[\\d]+.[\\d]+,[\\d]+.[\\d]+,N,[\\d]+.[\\d]+|\\b([\\d]{2}).([\\d]{1}),M";
		
		Pattern pattern = Pattern.compile(spattern);
		Matcher mat = pattern.matcher(message);
		int counter = 0;
		String[] result = new String[360];
		
		
 		while (mat.find()){
 			String res = mat.group(0);
 			result[counter*4]=res.substring(6,16);
 			result[counter*4 + 1] = res.substring(17,26);
 			result[counter*4 + 2] = res.substring(29);
 			if(mat.find()) {
 				res = mat.group(0);
 				result[counter*4 + 3] = res;
 				mat.find();
 			}
 			counter++;	
		}
		
 		return result;
	}
public static String coordCalc(String info , int ne) {
	String result = "" ;
	String d = info.substring(ne, 2 + ne);
	String m = info.substring(2+ne, 4+ne);
	String s = String.valueOf((int)Math.round((Float.valueOf("0" + info.substring(4+ne))*60)));
	if (s.length()==1) {
		s = "0" + s;
	}
	result = d + m +s;
	return result;
}
public static boolean TimeDifference(String time1, String time2) {
	String cTime1 = time1.substring(0, 6);
	String cTime2 = time2.substring(0, 6);
	int intTime1 =Integer.parseInt(cTime1);
	int intTime2 =  Integer.parseInt(cTime2);
	
	int k =(intTime2/100- intTime1/100);
	if(k!=0) {
		int timeDif = intTime2 - intTime1 - k*40;
		
		if (timeDif>9) return true;
		else return false;
	}
	else {
		int timeDif = intTime2 - intTime1;
		
		if (timeDif>9) return true;
		else return false;
	}
}
public static String[] validGps(String[] input) {
	String[] result = new String[36];
	boolean a = true;
	int counter = 0;
	int index = 0;
	while(a) {
		for(int i=counter; i<90; i++) {
			
			if (TimeDifference(input[counter*4], input[i*4])) {
				if(counter==0) {
					result[4*index] = input[counter*4];
					result[4*index+1] = input[counter*4 + 1];
					result[4*index+2] = input[counter*4 + 2];
					result[4*index+3] = input[counter*4 + 3];
					index++;
				}
				result[4*index] = input[i*4];
				result[4*index+1] = input[i*4 + 1];
				result[4*index+2] = input[i*4 + 2];
				result[4*index+3] = input[i*4 + 3];
				index++;
				counter = i;
				
			}else {
				if(i==8) a = false;
			}
		}
		
		
	}
	
	return result;
}

public static String[] gpsTranslate(String[] info) {
	int points = 9;
	String[] result = new String[points];
	String[] e = new String[points];
	String[] n = new String[points];
	for (int i=0; i<points; i++) {
		e[i] = coordCalc(info[2+i*4], 1);
		n[i] = coordCalc(info[1+i*4], 0);
		result[i] = e[i] + n[i];
	}
	

	
	return result;
}
	public static void gpsread(Modem modem, String mes) throws IOException {
		int k;
		String result="";
		long start = System.currentTimeMillis();
		send(mes+"R=1003090\r",modem);
		
		int a =0;
			for (;;) {
				try {
				k=modem.read();
				if (a==0) {
					long end = System.currentTimeMillis();
					ping = end - start;
					pingUpdated = true;
				}
				a++;
				if (k==-1) break;
				
				result += String.valueOf((char)k);
				
				} catch (Exception x) {
				break;
				}
			}
		stringRes = result;
		updated = true;
		
		String[] res = gpsTranslate(validGps(cleanGps(result)));
		
		
		String clCode = mes.substring(0,5);
		
		imageread(modem,  clCode+"T="+res[0]+"T="+res[1]+"T="+res[2]+"T="+res[3]+"T="+res[4]+"T="+res[5]+"T="+res[6]+"T="+res[7]+"T="+res[8]+"\r" , 130000);

	}
	public static String[] cleanEr(String message, boolean start) {
		String[] result = new String[2];
		final int INF = 31;
		final int RES = 49;
		final int STRT = 192+13;
		
		if(start) {
			result[0] = message.substring(STRT + INF, STRT + INF + 16);
			result[1] = message.substring(STRT + RES, STRT + RES + 3);
		}else {
			result[0] = message.substring(INF,INF + 16);
			result[1] = message.substring(RES, RES + 3);
		}
		return result;
	}
	public static boolean findErr(String[] result) {
		char[] charArr = new char[16];
		int expected = Integer.parseInt(result[1]);
		int[] res = new int[15];
		 
		int sum = 0;
		for (int i=0; i<16; i++) {
			charArr[i] = result[0].charAt(i);
		}
		
		
		for (int i=0; i <15; i++) {
			if(i==0) sum = charArr[i]^charArr[i+1];
			else sum ^= charArr[i+1];
		}
		
		if (sum!= expected) return false;
		else return true;
		
	}
	
	public static void saveString(String string, String file) throws IOException {
		
        FileWriter w = new FileWriter(file, true);
        w.write(string);
        w.write(" ");
        w.close();
        
	}
	public static void erRead(Modem modem, String message, String Nack , int a) throws IOException {
		int k;
		Vector<String> wrongPackets = new Vector<>(10,10);
		
		long start = System.currentTimeMillis();
		send(message,modem);
		String result = "";
		int o = 0;
		for (;;) {
			try {
				
			k=modem.read();
			if (o==0) {
				long end = System.currentTimeMillis();
				ping = end - start;
				pingUpdated = true;
			}
			o++;
			if (k==-1) break;
			
			result += String.valueOf((char)k);
			
			} catch (Exception x) {
			break;
			}
		}
		
		String[] res = null;
		if(a>0) res= cleanEr(result, false);
		else res = cleanEr(result, true);
		
		stringRes = result;
		updated = true;
		
		boolean endOfErr = findErr(res);
		if (!endOfErr) {
			wrongPackets.add(res[0]);
			
			numberOfBits +=16*8;
		}else {
			numberOfBits +=16*8;
			if(wrongBits >0) {
				bitErrorRate = (double)wrongBits/(double)numberOfBits;
				BERupdated = true;
			}
				
		}
		String[] errors = new String[8];
		Arrays.fill(errors, "");
		int counter = 0;
		boolean err = false;
		TimesCalledAck ++;
		int prob = 0;
		while(!endOfErr) {
			prob++;
			TimesCalledNack ++;
			err = true;
			errors[counter] = res[0];
			counter++;
			result = "";
			long start2 = System.currentTimeMillis();
			send(Nack,modem);
			
			for (;;) {
				try {
				k=modem.read();
				if(a==0) {
					long end = System.currentTimeMillis();
					ping = end- start2;
					pingUpdated = true;
				}
				if (k==-1) break;
				
				result += String.valueOf((char)k);
				
				} catch (Exception x) {
				break;
				}
			}
			
			stringRes = result;
			updated = true;
			res = cleanEr(result, false);
			endOfErr = findErr(res);
			if (!endOfErr) {
				wrongPackets.add(res[0]);
				numberOfBits +=16*8;
			}else {
				numberOfBits +=16*8;
				System.out.println("The Number of wrong packages are " +wrongPackets.size() );
				for(int j=0; j<wrongPackets.size(); j++) {
					
					for(int i=0; i<16; i++) {
						wrongBits += Integer.bitCount(wrongPackets.get(0).charAt(i)^res[0].charAt(i));
						
						if(Integer.bitCount(wrongPackets.get(0).charAt(i)^res[0].charAt(i))>0)
							saveString(String.valueOf(wrongPackets.get(0).charAt(i)), "left.txt");
					}
					//saveString(" ", "left.txt");
				}
				wrongPackets.clear();
				bitErrorRate = (double)wrongBits/(double)numberOfBits;
				BERupdated = true;
			}
			
		}
		saveString(Integer.toString(prob), "prob.txt");
		saveString(Float.toString((float)TimesCalledNack/(float)TimesCalledAck)+ " ", "nack_ack.txt");
		saveString(res[0], "right.txt");
		
	}
}
