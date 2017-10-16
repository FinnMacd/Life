package life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Life extends JPanel implements Runnable, MouseMotionListener, MouseWheelListener, MouseListener, KeyListener {

	public static final int SCREEN_WIDTH = 1000, SCREEN_HEIGHT = 1000;
	
	public static double SCALE = 1;

	public static final int width = SCREEN_WIDTH * (int)SCALE, height = SCREEN_HEIGHT * (int)SCALE;
	
	private static int x = 0, y = 0;
	
	private static final int start = 1, end = 3; 

	private short[] screen, comp;

	private int mx, my, ly, lx, hy, hx;

	int gen, life, ups;

	boolean auto, run = false, mp;

	public static void main(String[] args) {
		JFrame j = new JFrame("Life");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setResizable(false);
		j.add(new Life());
		j.pack();
		j.setLocationRelativeTo(null);
		j.setVisible(true);
		
	}

	public Life() {
		screen = new short[(int)(width * height)];
		//Random r = new Random();
//		for (int i = 0; i < x * y; i++) {
//			if (i<x)screen[i]=1;
//			else if(i>(y-1)*x)screen[i] = 1;
//			else if(i % x == 0)screen[i] = 1;
//			else if(i % x == (x-1))screen[i] = 1;
//		}
		//screen[(int)(((height-1)/2)*width)] = 1;
		//screen[(int)((((height-1)/2)*width)+1)] = 1;
		screen[0] = 1;
		screen[1] = 1;
		
//		ly = ((height-1)/2)-3;
//		hy = ((height-1)/2)+3;
//		lx = -1;
//		hx = 3;
		
		lx = -1;
		hx = 5;
		ly = -1;
		hy = 5;
		
		comp = new short[(int)(width * height)];
		new Thread(this).start();
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

		requestFocus();
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
	}

	public void run() {

		auto = true;

		long timer = System.currentTimeMillis();
		int updates = 0, pu = 0;

		while (true) {

			
			if (run){
				gen();
				updates ++;
			}
			repaint();
			
			if (System.currentTimeMillis() - timer >= 500) {
				timer += 500;
				ups = updates + pu;
				pu = updates;
				updates = 0;
			}

		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		
		for (int xc = x; xc < width/SCALE; xc++) {
			for (int yc = y; yc < height/SCALE; yc++) {
				if (screen[(int)(yc*SCALE)*width + (int)(xc*SCALE)] == 0)
					 //g.drawRect((i % x) * (WIDTH/x), (i / y) * (HEIGHT/y), WIDTH/x, HEIGHT/y);
					;
				else
					//g.fillRect((int)((i % x) * (x/WIDTH)), (int)((i / x) * (y / HEIGHT)), (int)(x/WIDTH), (int)(y / HEIGHT));
					g.fillRect((xc), (yc), 1, 1);
			}
		}

		g.setColor(Color.red);
		g.setFont(new Font("TimesNewRoman", Font.BOLD, 24));
		g.drawString("Gen: " + Integer.toString(gen), 20, 20);
		g.drawString("Life: " + Integer.toString(life), 20, 50);
		if (auto) g.drawString("Ups: " + Integer.toString(ups), SCREEN_WIDTH-150, 20);

	}

	public void gen() {
		life = 0;
		gen++;
		
		int tlx = width; 
		int thx = 0; 
		int tly = height; 
		int thy = 0; 
		
		for (int i = 0; i < width * height; i++) {
			
			int xc = i%width;
			int yc = i/width;
			if(xc<lx||xc>hx||yc<ly||yc>hy)continue;
			
			short pop = 0;

			boolean r, l, t, b;

			r = l = t = b = false;

			l = i % width != 0;
			r = i % width != width - 1;
			t = i > width - 1;
			b = i < (height - 1) * width;

			if (r) pop+=screen[i + 1];
			if (l) pop+=screen[i - 1];
			if (t) pop+=screen[i - (int)width];
			if (b) pop+=screen[i + (int)width];
			if (r && t) pop+=screen[i - ((int)width - 1)];
			if (r && b) pop+=screen[i + (int)width + 1];
			if (l && t) pop+=screen[i - (int)width - 1];
			if (l && b) pop+=screen[i + ((int)width - 1)];

			if (screen[i] == 0 && pop == start) {
				comp[i] = 1;
				life++;
				
				tlx = (xc<tlx)?xc:tlx;
				thx = (xc>thx)?xc:thx;
				tly = (yc<tly)?yc:tly;
				thy = (yc>thy)?yc:thy;
				
			} else if (screen[i] == 1 && pop >= end){
				comp[i] = 0;
				tlx = (xc<tlx)?xc:tlx;
				thx = (xc>thx)?xc:thx;
				tly = (yc<tly)?yc:tly;
				thy = (yc>thy)?yc:thy;
			}else if (screen[i] == 1) {
				comp[i] = 1;
				life++;
			}

		}
		
		lx = tlx-2;
		ly = tly-2;
		hx = thx+2;
		hy = thy+2;

		screen = comp.clone();
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent e) {
		mp = true;
		screen[(int)((mx/SCALE) + (my/SCALE) * width)] = 1;
	}

	public void mouseReleased(MouseEvent e) {
		mp = false;

	}

	public void mouseDragged(MouseEvent e) {

		mx = e.getX();
		my = e.getY();
		if(mx < 0 || mx > SCREEN_WIDTH || my < 0 || my > SCREEN_HEIGHT)mx = my = 0;
		screen[(int)((mx/(SCREEN_WIDTH/width)) + (my/(SCREEN_HEIGHT/height)) * width)] = 1;
	}

	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		if(mx < 0 || mx > SCREEN_WIDTH || my < 0 || my > SCREEN_HEIGHT)mx = my = 0;
	}

	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			
		case KeyEvent.VK_SPACE: run = !run;break;
		
		case KeyEvent.VK_ENTER: gen();break;
		
		case KeyEvent.VK_LEFT: x-=SCALE*3;break;
		case KeyEvent.VK_RIGHT: x+=SCALE*3;break;
		
		case KeyEvent.VK_UP: y-=SCALE*3;break;
		case KeyEvent.VK_DOWN: y+=SCALE*3;break;
			
		}
		
		if(x < 0)x = 0;
		if(y < 0)y = 0;

	}

	public void keyReleased(KeyEvent arg0) {

	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		
		SCALE += e.getPreciseWheelRotation()*0.01;
		
		if(SCALE < 1)SCALE = 1;
		
	}

}
