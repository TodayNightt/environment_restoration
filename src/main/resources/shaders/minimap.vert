#version 330

layout(location=0)in uint aPosition;

uniform mat4 projectionMatrix;
uniform vec2 viewPort;

out vec2 vTexCoord;

vec2 uv[4]= vec2[4](
vec2(0.0,1.0),
vec2(1.0,1.0),
vec2(0.0,0.0),
vec2(1.0,0.0)
);

void main() {
    float x = (float((aPosition >> 7u) & 0x1Fu)/20.0) * viewPort.x;
    float y = (float((aPosition >> 2u) & 0x1Fu)/20.0) * viewPort.x;
    vTexCoord = uv[aPosition & 0x3u];
    gl_Position = projectionMatrix * vec4(x,y,0.0, 1.0);
}
