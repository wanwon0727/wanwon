//  * UML  * �˻��ؼ� ����
// Refactoring
// 1.�鿩���� �ϱ�. ������ �� �Ǿ� �־�� ���߿� �ٽ� �ʿ�κа������� ������.���������� ����.
// 2.
// Refac
package com.game.shooting;
import java.awt.*;
import java.awt.event.*; // KeyListener�� ����Ϸ��� �ʿ��ѵ�
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*; // JFrame ������ �ʿ��ѵ�

// Refactoring.   git hub??
// GUI�� �����Ϸ��� JFrame�� �� ����Ϸ� -_-;
// KeyListener. Ű���忡�� Ű ������ ó���� �� ����ϴµ�...
// Runnable : ������(Thread)�� ������� implements �ؾ� �Ѵٳ�..
public class Shoot extends JFrame implements Runnable, KeyListener {
 private BufferedImage bi = null;
 private ArrayList msList = null; // ArrayList �� ���� ����.
 private ArrayList enList = null; // ArrayList �� ���� ����.
 // ����Ű�� �߻�Ű ���� ��� �����ε�...
 private boolean left = false, right = false, up = false, down = false, fire = false;
 private boolean start = false, end = false;
 // w, h �� ����â ����, ���� ����
 // x, y �� �÷��̾��� ��ǥ
 // xw, xh �� �÷��̾��� ũ��
 private int w = 300, h = 500, x = 130, y = 450, xw = 50, xh = 20;
 public Shoot() { // Shoot Ŭ���� ������
	 
	 
  bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
  msList = new ArrayList(); // ArrayList ��ü�� �ϳ� ����.
  enList = new ArrayList(); // ArrayList ��ü�� �ϳ� ����. 
  this.addKeyListener(this);
  this.setSize(w, h); // â�� ũ�⸦ �����ϳ�����.. Ȥ�� ����/���� -> Ȯ���ϱ� ����
  this.setTitle("Shooting Game by SWW");
  this.setResizable(false); // âũ�� ���� �������� ����(true: ����, false : �Ұ���)
  //this.setResizable(true);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // â�� x��ư ������ ���α׷��� ���� ����ǰ� �ϴ� ���. �� ������ �ۼ����� ������ ȭ�鿡���� ������ �ʾƵ� ���α׷��� ��� ���ư��� �ִ� ������ �����.
  this.setVisible(true);  // â�� ȭ�鿡 �������� ���� ����(true : ������, false : �Ⱥ����� - �ؿ��� ���ܹ߻��ϴ��� --> ����ó���� ���س���)
 // this.setVisible(false);
	  
 }
 
 public void run() {
  try {
   int msCnt = 0;
   int enCnt = 0;
   while(true) { // ���ѷ��� -_-a ������ �̷������� ¥�ٴ�
   // Thread.sleep(10);
    Thread.sleep(10); // ��� ���� ��Ű�� �޼ҵ�, �־���? -> ���Ӽӵ� ���߰�, ������ �ϰ�
    if(start) { // start�� ���̸� �Ʒ���
     if(enCnt > 20) { // encnt�� 2000���� ũ�� �Ʒ����� ����
      enCreate();
      enCnt = 0;
     }
     if(msCnt >= 100) {
      fireMs();
      msCnt = 0;
     }
     msCnt += 10;
     enCnt += 10;
     keyControl();
     crashChk();
    }
    draw();
   }
  } catch(Exception e) {
   e.printStackTrace();
  }
 }
 public void fireMs() {
  if(fire) {
   if(msList.size() < 100) {
    Ms m = new Ms(this.x, this.y);
    msList.add(m);
   }
  }
 }
 public void enCreate() {
  for(int i = 0; i < 9; i++) {
   double rx = Math.random() * (w - xw);
   double ry = Math.random() * 50;
   Enemy en = new Enemy((int)rx, (int)ry);
   enList.add(en);
  }
 }
 public void crashChk() {
  Graphics g = this.getGraphics();
  Polygon p = null;
  for(int i = 0; i < msList.size(); i++) {
   Ms m = (Ms)msList.get(i);
   for(int j = 0; j < enList.size(); j++) {
    Enemy e = (Enemy)enList.get(j);
    int[] xpoints = {m.x, (m.x + m.w), (m.x + m.w), m.x};
    int[] ypoints = {m.y, m.y, (m.y + m.h), (m.y + m.h)};
    p = new Polygon(xpoints, ypoints, 4);
    if(p.intersects((double)e.x, (double)e.y, (double)e.w, (double)e.h)) {
     msList.remove(i);
     enList.remove(j);
    }
   }
  }
  for(int i = 0; i < enList.size(); i++) {
   Enemy e = (Enemy)enList.get(i);
   int[] xpoints = {x, (x + xw), (x + xw), x};
   int[] ypoints = {y, y, (y + xh), (y + xh)};
   p = new Polygon(xpoints, ypoints, 4);
   if(p.intersects((double)e.x, (double)e.y, (double)e.w, (double)e.h)) {
    enList.remove(i);
    start = false;
    end = true;
   }
  }
 }
 
 public void draw() {
  Graphics gs = bi.getGraphics();
  gs.setColor(Color.white);
  gs.fillRect(0, 0, w, h);
  gs.setColor(Color.black);
  gs.drawString("Enemy ��ü�� : " + enList.size(), 180, 50);
  gs.drawString("Ms ��ü�� : " + msList.size(), 180, 70);
  gs.drawString("���ӽ��� : Enter", 180, 90);
  
  if(end) {
   gs.drawString("G A M E     O V E R", 100, 250);
  }
  
  gs.fillRect(x, y, xw, xh);
   
  for(int i = 0; i < msList.size(); i++) {
   Ms m = (Ms)msList.get(i);
   gs.setColor(Color.blue);
   gs.drawOval(m.x, m.y, m.w, m.h);
   if(m.y < 0) msList.remove(i);
   m.moveMs();
  }
  gs.setColor(Color.black);
  for(int i = 0; i < enList.size(); i++) {
   Enemy e = (Enemy)enList.get(i);
   gs.fillRect(e.x, e.y, e.w, e.h);
   if(e.y > h) enList.remove(i);
   e.moveEn();
  }
  
  Graphics ge = this.getGraphics();
	  try{
	  ge.drawImage(bi, 0, 0, w, h, this);
	  }
	  catch(java.lang.NullPointerException e){
		  //java.lang.NullPointerException ���ܰ� ����� ������ �ڵ�
		  // ������ �� ��© �ž�..
		  // �ƴϴ�.. �ֿܼ� �޼����� ����غ���~~
		  System.out.println("java.lang.NullPointerException��� ���ܹ߻�~~ �ð����� ���ְ� �ٲ�����");
	  }
 }
 
 public void keyControl() {
  if(0 < x) {
   if(left) x -= 3;
  }
  if(w > x + xw) {
   if(right) x += 3;
  }
  if(25 < y) {
   if(up) y -= 3;
  }
  if(h > y + xh) {
   if(down) y += 3;
  }
 }
 
 public void keyPressed(KeyEvent ke) {
  switch(ke.getKeyCode()) {
  case KeyEvent.VK_LEFT:
   left = true;
   break;
  case KeyEvent.VK_RIGHT:
   right = true;
   break;
  case KeyEvent.VK_UP:
   up = true;
   break;
  case KeyEvent.VK_DOWN:
   down = true;
   break;
  case KeyEvent.VK_A:
   fire = true;
   break;
  case KeyEvent.VK_ENTER:
   start = true;
   end = false;
   break;
  }
 }
 
 public void keyReleased(KeyEvent ke) {
  switch(ke.getKeyCode()) {
  case KeyEvent.VK_LEFT:
   left = false;
   break;
  case KeyEvent.VK_RIGHT:
   right = false;
   break;
  case KeyEvent.VK_UP:
   up = false;
   break;
  case KeyEvent.VK_DOWN:
   down = false;
   break;
  case KeyEvent.VK_A:
   fire = false;
   break;
  }
 }
 
 public void keyTyped(KeyEvent ke) {}
 
 public static void main(String[] args) {
  Thread t = new Thread(new Shoot()); // ������ �ϳ� ����. ShootŬ������ ������ ������ ����?
  t.start(); // �����带 �����ϴ� �� ����..
 }
}

class Ms {
 int x;
 int y;
 int w = 5;
 int h = 5;
 public Ms(int x, int y) {
  this.x = x;
  this.y = y;
 }
 public void moveMs() {
  y--;
 }
}

class Enemy {
 int x;
 int y;
 int w = 10;
 int h = 10;
 public Enemy(int x, int y) {
  this.x = x;
  this.y = y;
 }
 public void moveEn() {
  y++;
 } 
}
