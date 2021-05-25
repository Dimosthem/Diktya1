package ergasia;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
public class ImageWindow extends JPanel {
	Image image ;
	
	public ImageWindow() {
		
	}
	public ImageWindow(Image image) {
		this.image = image;
		
	}
	public ImageWindow(String image) throws IOException {
		this.image = ImageIO.read(new File(image));
	}
	public void setImage(Image image) {
		this.image = image;

	}
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    if (image !=null)
	    	g.drawImage(image,0,0,null);
	    else {
	    	g.setColor(getBackground());
	    	g.fillRect(0, 0, getWidth(), getHeight());
	    }
	}	
}
