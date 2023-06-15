#version 310 es
//#ifdef GL_ES
precision mediump float;
precision mediump int;
//#endif

out vec4 fragColor;

in vec3 vPosition;
in vec2 vTexCoord;

uniform vec3 size;

float line(float x ,float line_width, float edge_width){
    return smoothstep(x-line_width/2.0-edge_width, x-line_width/2.0, x) - smoothstep(x+line_width/2.0, x+line_width/2.0+edge_width, x);
}

float line(vec3 pos,vec3 size,float lineWidth){
    float line = 0.0;
    float middle = 0.0;
    float leftMost = middle - (0.5*size.x);
    for(float i = 1.0; i < size.x; i+=1.0){
        line += (step(leftMost+i-lineWidth,pos.x)- step(leftMost+i+lineWidth,pos.x));
    }
    float topMost = middle - (0.5*size.y);
    for(float i = 1.0; i < size.y; i+=1.0){
        line += (step(topMost+i-lineWidth,pos.y)- step(topMost+i+lineWidth,pos.y));
    }
    float frontMost = middle + (0.5*size.z);
    for(float i = 1.0; i < size.z; i+=1.0){
        line += step(frontMost-i-lineWidth,pos.z)- step(frontMost-i+lineWidth,pos.z);
    }
    return 1.0-line;
}

void main()
{
    vec2 st = vTexCoord;
    vec3 c = mix(vec3(0.5,0.1,0.5),vec3(0.0,0.3,0.6),vPosition.x);
    vec3 color =vec3(1.0);
    color *= line(vPosition,size,0.03);
    fragColor = vec4(color,1.0);
}
