#version 330

layout(location=0) in uint aPosition;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform float fValue[1];


out vec3 vColor;
out vec2 vTexCoord;

vec2 uv[4]= vec2[4](
vec2(0.0,1.0),
vec2(1.0,1.0),
vec2(0.0,0.0),
vec2(1.0,0.0)
);




//https://github.com/Hopson97/open-builder/blob/master/shaders/chunk_vertex.glsl
void main()
{
    float textureRow = float(fValue[0]);
    int type = int(aPosition & 0x3u);
    float xOffset = float((mod(type,textureRow)) / float(textureRow));
    float yOffset = float(floor(type/textureRow) / textureRow);
    vec2 offset = vec2(xOffset,yOffset);
    vTexCoord = (uv[(aPosition >> 2) & 0x3u] /textureRow) + offset;


    float x = float((aPosition >> 15) & 0x1Fu);
    float z = float((aPosition >> 10) & 0x1Fu);
    float y = float((aPosition >> 4) & 0x3Fu);
    vColor = vec3(x,y,z);
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(x,y,z,1.0);
}