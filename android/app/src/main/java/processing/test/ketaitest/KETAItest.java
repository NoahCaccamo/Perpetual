package processing.test.ketaitest;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ketai.camera.*; 
import ketai.cv.facedetector.*; 
import ketai.data.*; 
import ketai.net.*; 
import ketai.net.bluetooth.*; 
import ketai.net.nfc.*; 
import ketai.net.nfc.record.*; 
import ketai.net.wifidirect.*; 
import ketai.sensors.*; 
import ketai.ui.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class KETAItest extends PApplet {












KetaiCamera cam, cam2;

float charge = 50;

int maxCharge = 100;

float overCharge;

boolean canUpgrade = true;

PImage bg, mux, snapshot;
int Br;
boolean stopIn;
float total;

public void setup() {
  
  frameRate(24);
  orientation(PORTRAIT);
  cam2 = new KetaiCamera(this, 320, 240, 24); // 1
  cam = new KetaiCamera(this, 1, 1, 1); // 1
  imageMode(CENTER);
  cam2.start();
  cam2.setCameraID(1);
  cam.start();
  cam2.manualSettings();
}



public void draw() {

  if (cam2.isStarted() == false) {
    cam2.start();
  }
  if (cam.isStarted() == false) {
    cam.start();
  }

  if (cam2.isStarted() && stopIn == false) {
    cam2.loadPixels();                                         // 3                                      // 5

    Br = 0;
    for (int i= 0; i < cam2.pixels.length; i++)
    {

      Br += red(cam2.pixels[i]);
      Br += green(cam2.pixels[i]);
      Br += blue(cam2.pixels[i]);                     // 8                      // 10
    }
    total = Br/(3*cam2.pixels.length);  
    println(total);
    if (total > 100) {
      if (charge < maxCharge) {
        charge += 1;
      } else if (canUpgrade == true) {
        overCharge += 1;
      } 
      if (overCharge >= maxCharge * 2 && canUpgrade == true) {
        maxCharge = maxCharge * 2;
        canUpgrade =false;
      }
    }
  }
  if (touchIsStarted) {
    //stopIn = true;
    background(150);
    if (charge > 0) {
      cam.enableFlash();
      charge -= 1.3f;
      overCharge = 0;
    } else {
      cam.disableFlash();
    }
  } else {
    stopIn = false;
    background(0);
    cam.disableFlash();
  }
  if (charge < 0) {
    charge = 0;
  }
  if (charge > maxCharge) {
    charge = maxCharge;
  }

fill(0xffB6B904);
rect(0, height, map(total, 0, 255, 0, width), -height);

  fill(255);
  textAlign(CENTER);
  textSize(150);
  text(PApplet.parseInt(charge) + " u", width/2, 150);
    textSize(40);
  text(total, 100, 200);


  fill(255);
  rect(0, height, width, -height + height/6);
rectMode(CENTER);
noStroke();
rect(width/2, height/6, width/9, height/23);
rectMode(CORNER);
stroke(0);
  fill(0);
  rect(0 + width/23, height -width/23, width - ((width/23)*2), -height + height/6 + ((width/23)*2));

  if (charge > maxCharge/2) {
    fill(0, 255, 0);
  } else if (charge > maxCharge/4) {
    fill(255, 255, 0);
  } else {
    fill(255, 0, 0);
  }
  float barSize;
  barSize = map(charge, 0, maxCharge, 0, -height + height/6 + height/23);
  rect(0 + width/23, height - width/23, width - ((width/23)*2), barSize);
}

public void onCameraPreviewEvent()
{
  cam2.read();
}

public void touchStarted() {
  println("touch started");
}

public void touchMoved() {
  println("touch moved");
}

public void touchEnded() {
  println("touch ended");
}
  public void settings() {  fullScreen(); }
}
