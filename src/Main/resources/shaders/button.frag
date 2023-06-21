#version 310 es

precision mediump float;

in vec2 vPosition;
in vec3 vColor;
out vec4 fragColor;
//in vec2 vTexCoord;

void main() {
    fragColor = vec4(vColor, 1.0);
}
