package ergasia;

import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Graph extends JPanel{
	protected volatile Vector<Integer> points = new Vector<>(10,1); 
	protected int X;
	protected int Y;
	protected int width;
	protected int height;
	protected int max=0;
	protected int median=0;
	public Graph(){
		
	}
	public Graph(int X, int Y, int width, int height) {
		this.X = X;
		this.Y = Y;
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
	}
	public void addPointFromFile(String fileName ) throws Exception {
		FileReader fr = new FileReader(fileName);
		String result = "";
		for(;;) {
			int k;
			k = fr.read();
			if (k==-1) break;
			
			result += String.valueOf((char)k);
		}
		String spattern = "[\\d]{2,4}";
		Pattern pattern = Pattern.compile(spattern);
		Matcher mat = pattern.matcher(result);
		while(mat.find()) {
			points.add(Integer.parseInt(mat.group(0)));
		}
		Vector<Integer>copyPoints = new Vector<>(points); 
		Collections.sort(copyPoints);
		max = copyPoints.get(copyPoints.size()-1);
		median = copyPoints.get((copyPoints.size()-1)/2);
		
	}
	public void addPoint(int point) {
		points.add(point);
	}
	public int calcY(int point) {
		float dif = (float) (1.0 - (float)point/(float)max); 
		
		return (int)(Y+ height/20  + (5*height/6 - height/20)*dif);
	}
	public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){

        //creates a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();

        //set the stroke of the copy, not the original 
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(x1, y1, x2, y2);

        //gets rid of the copy
        g2d.dispose();
}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.drawLine(X+width/20,Y+ height -height/6, X + width-width/20, Y + height-height/6);
	    g2.drawLine(X+ width/20, Y+5*height/6, X+ width/20 , Y + height/40 );
	    g2.drawLine(X+width/20 - width/100, Y+height/40 + height/50, X+width/20, Y+ height/40);
	    g2.drawLine(X+width/20 + width/100, Y+height/40 + height/50, X+width/20, Y+ height/40);
	    g2.drawLine(X + width - width/20 - width/100,Y + height-height/6-height/60, X + width-width/20, Y + height-height/6 );
	    g2.drawLine(X + width - width/20 - width/100,Y + height-height/6+height/60, X + width-width/20, Y + height-height/6 );
		g2.drawString("0", X+ width/20 - width/25, Y + height - height/6 + height/100);  
		Color dash = new Color(200, 200, 200);
		Color black = new Color(0,0,0);
		g2.setColor(dash);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 10)); 
		drawDashedLine(g, X+width/20,Y+ height/20, X + width-width/20, Y + height/20);
		g2.setColor(black);
		g2.drawString(Integer.toString(max), X,Y+ height/20 + height/100);
		g2.drawString(Integer.toString(max/2), X ,Y+ height/20 + height/100 + (5*height/6 - height/40)/2 );
		float dif = (float) (1.0 - (float)median/(float)max); 
		g2.drawString(Integer.toString(median), X,Y+ height/20 + height/100 + (5*height/6 - height/40)*dif);
		g2.drawString(Integer.toString((int) (0.75*max)),X,(int) (Y+ height/20 + height/100 + (5*height/6 - height/40)*0.25));
		g2.drawString(Integer.toString((int) (0.25*max)),X,(int) (Y+ height/20 + height/100 + (5*height/6 - height/40)*0.75));
		g2.setColor(dash);
		drawDashedLine(g, X+width/20,Y+ height/20  + (5*height/6 - height/40)/2, X + width-width/20, Y+ height/20 + (5*height/6 - height/40)/2 );
		drawDashedLine(g, X+width/20,(int)(Y+ height/20  + (5*height/6 - height/40)*dif), X + width-width/20, (int)(Y+ height/20  + (5*height/6 - height/40)*dif));
		drawDashedLine(g, X+width/20,(int)(Y+ height/20  + (5*height/6 - height/40)*0.25), X + width-width/20, (int)(Y+ height/20  + (5*height/6 - height/40)*0.25));
		drawDashedLine(g, X+width/20,(int)(Y+ height/20  + (5*height/6 - height/40)*0.75), X + width-width/20, (int)(Y+ height/20  + (5*height/6 - height/40)*0.75));
		
	}
}
