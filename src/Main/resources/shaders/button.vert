#version 310 es

precision mediump float;

layout(location=0)in vec2 aPosition;
//layout(location=1)in vec2 aTexCoord;

uniform mat4 projectionMatrix;
uniform float resizeFactor;
uniform vec3 currentColor;

out vec2 vPosition;
out vec3 vColor;
//out vec2 vTexCoord;

void main() {
    vColor = currentColor;
    //    vTexCoord = aTexCoord;
    vPosition = aPosition;
    gl_Position = projectionMatrix * vec4(aPosition *resizeFactor, 0.0, 1.0);
}
