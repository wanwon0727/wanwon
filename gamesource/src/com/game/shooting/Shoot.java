//  * UML  * 검색해서 공부
// Refactoring
// 1.들여쓰기 하기. 정리가 잘 되어 있어야 나중에 다시 필요부분가져오기 수월함.가독성좋게 만듦.
// 2.
// Refac
package com.game.shooting;
import java.awt.*;
import java.awt.event.*; // KeyListener를 사용하려면 필요한듯
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*; // JFrame 쓸려면 필요한듯

// Refactoring.   git hub??
// GUI를 구현하려면 JFrame을 꼭 상속하래 -_-;
// KeyListener. 키보드에서 키 눌린거 처리할 때 사용하는듯...
// Runnable : 쓰레드(Thread)를 만들려면 implements 해야 한다네..
public class Shoot extends JFrame implements Runnable, KeyListener {
 private BufferedImage bi = null;
 private ArrayList msList = null; // ArrayList 를 담을 변수.
 private ArrayList enList = null; // ArrayList 를 담을 변수.
 // 방향키랑 발사키 정보 담는 변수인듯...
 private boolean left = false, right = false, up = false, down = false, fire = false;
 private boolean start = false, end = false;
 // w, h 는 실행창 가로, 세로 넓이
 // x, y 는 플레이어의 좌표
 // xw, xh 는 플레이어의 크기
 private int w = 300, h = 500, x = 130, y = 450, xw = 50, xh = 20;
 public Shoot() { // Shoot 클래스 생성자
	 
	 
  bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
  msList = new ArrayList(); // ArrayList 객체를 하나 만듦.
  enList = new ArrayList(); // ArrayList 객체를 하나 만듦. 
  this.addKeyListener(this);
  this.setSize(w, h); // 창의 크기를 결정하나보다.. 혹시 가로/세로 -> 확실하군 ㅎㅎ
  this.setTitle("Shooting Game by SWW");
  this.setResizable(false); // 창크기 조절 가능한지 결정(true: 가능, false : 불가능)
  //this.setResizable(true);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창의 x버튼 누르면 프로그램도 같이 종료되게 하는 방법. 이 구문을 작성하지 않으면 화면에서는 보이지 않아도 프로그램은 계속 돌아가고 있는 문제가 생긴다.
  this.setVisible(true);  // 창을 화면에 보여줄지 말지 결정(true : 보여줌, false : 안보여줌 - 밑에서 예외발생하던데 --> 예외처리좀 잘해놓지)
 // this.setVisible(false);
	  
 }
 
 public void run() {
  try {
   int msCnt = 0;
   int enCnt = 0;
   while(true) { // 무한루프 -_-a 아직도 이런식으로 짜다니
   // Thread.sleep(10);
    Thread.sleep(10); // 잠시 정지 시키는 메소드, 왜쓰지? -> 게임속도 늦추고, 빠르게 하고
    if(start) { // start가 참이면 아래로
     if(enCnt > 20) { // encnt가 2000보다 크면 아래구문 실행
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
  gs.drawString("Enemy 객체수 : " + enList.size(), 180, 50);
  gs.drawString("Ms 객체수 : " + msList.size(), 180, 70);
  gs.drawString("게임시작 : Enter", 180, 90);
  
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
		  //java.lang.NullPointerException 예외가 생기면 실행할 코드
		  // 하지만 난 안짤 거야..
		  // 아니다.. 콘솔에 메세지는 출력해볼까~~
		  System.out.println("java.lang.NullPointerException라는 예외발생~~ 시간나면 멋있게 바꾸지뭐");
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
  Thread t = new Thread(new Shoot()); // 쓰레드 하나 생성. Shoot클래스를 가지고 쓰레드 생성?
  t.start(); // 쓰레드를 시작하는 것 같음..
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
