#version 330

out vec4 fragColor;

in vec3 vPosition;
in vec2 vTexCoord;

// uniform sampler2D tex;

void main()
{
    vec3 color = mix(vec3(0.5,0.1,0.5),vec3(0.0,0.3,0.6),vPosition.x);
    fragColor = vec4(color,1.0);
}