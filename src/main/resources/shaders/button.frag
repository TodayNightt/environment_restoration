#version 330

in vec2 vPosition;
//in vec3 vColor;
in vec2 vTexCoord;

uniform sampler2D tex;

out vec4 fragColor;

void main() {
    fragColor = texture(tex,vTexCoord);
}
