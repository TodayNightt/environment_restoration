#version 330

layout(location=0) in vec3 aPosition;
layout(location=1) in vec2 aTexCoord;
layout(location=2) in vec2 aUVOffset;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
//uniform float textureRow;

out vec3 vPosition;
//out vec2 vTexCoord;

void main()
{
//    vTexCoord = (aTexCoord/textureRow)+ aUVOffset;
    vPosition = aPosition;
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(aPosition,1.0);
}