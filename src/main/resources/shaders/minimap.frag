#version 330

in vec2 vTexCoord;
in vec3 vPosition;

uniform sampler2D tex;

out vec4 fragColor;

void main() {
    fragColor = texture(tex,vTexCoord);
}
