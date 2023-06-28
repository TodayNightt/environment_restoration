#version 330

layout(location=0) in uint aPosition;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
//uniform float textureRow;

out vec3 vPosition;
//out vec2 vTexCoord;

void main()
{
    float x = float((aPosition >> 11));
    float y = float((aPosition) & 0x3Fu);
    float z = float((aPosition >> 5) & 0x1Fu);
    vPosition = vec3(x,y,z);
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(x,y,z,1.0);
}