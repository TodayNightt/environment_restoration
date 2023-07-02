#version 330
out vec4 fragColor;

uniform sampler2D tex;
in vec2 vTexCoord;
in vec3 vColor;
void main()
{
    vec3 color = mix(vec3(0.5,0.1,0.5),vec3(0.0,0.3,0.6),vColor.x);
    fragColor = texture(tex,vTexCoord);
}