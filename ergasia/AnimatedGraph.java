package ergasia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Vector;

public class AnimatedGraph extends Graph {
	private int counter = 0;
	public AnimatedGraph(int x, int y , int width, int height) throws Exception {
		super(x, y, width, height);
		//super.addPointFromFile("ping.txt");
	}

	public void movePoints() {
		counter ++ ;
		
		if( counter!=1)
			return;
		AnimatedGraph mg = this;
		new Thread() {
			
			public void run() {
				
				int o=0;
				while(mg.points.size()>0) {
					o++;
					Vector<Integer> cv= new Vector<>(mg.points);
					Collections.sort(cv);
					mg.max = cv.get(cv.size()-1);
					mg.median = cv.get(cv.size()/2);
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mg.validate();
					mg.repaint();
					if(o%2 ==0&&o>1&&mg.points.size()>10) {
						points.remove(0);
					}
					
					
				}
				
			}
		}.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		float space = (float)(width - width/10)/(float)(points.size());
		
		int x0 = X + width/20;
		Color blue = new Color(0,0,255);
		g2.fillOval(x0 + (int)(space*(points.size()-1)), 500, 10,10);
		g2.setColor(blue);
		for(int i = 0; i<points.size(); i++) {
			if(i==0) {
				g2.fillOval(x0 + (int)(space*i), calcY(points.get(i)),5 ,5);
			}
			else {
				
				g2.drawLine(x0  + (int)(space*(i-1)), calcY(points.get(i-1)), x0 + (int)(space*i), calcY(points.get(i)));
				g2.fillOval(x0 + (int)(space*i), calcY(points.get(i)),5 ,5);
			}
		}
	}
}
