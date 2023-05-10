PImage img;

void setup() {
  size(400, 400,P3D);
  img = loadImage("Dirt_Texture.png");
}

void draw() {
  background(0);
noStroke();
  //noFill();
  textureMode(NORMAL);

  beginShape(TRIANGLE_STRIP);
    texture(img);
  vertex(0,0,0,0);
  vertex(0,200,0,1);
  vertex(200,0,1,0);
  vertex(200,200,1,1);
  endShape();
}
