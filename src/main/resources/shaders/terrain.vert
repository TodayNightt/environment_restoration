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

struct Vertex_Data
{
    vec3 position;
    uint uv;
};


Vertex_Data convert(uint vertex){
    Vertex_Data data;
    data.position.x = float((vertex >> 15) & 0x1Fu);
    data.position.y = float((vertex >> 4) & 0x3Fu);
    data.position.z = float((vertex >> 10) & 0x1Fu);
    data.uv = (vertex >> 2) & 0x3u;
    return data;
}






//https://github.com/Hopson97/open-builder/blob/master/shaders/chunk_vertex.glsl
void main()
{
    float textureRow = float(fValue[0]);
    int type = int(aPosition & 0x3u);
    float xOffset = float((mod(type,textureRow)) / float(textureRow));
    float yOffset = float(floor(type/textureRow) / textureRow);
    vec2 offset = vec2(xOffset,yOffset);

    Vertex_Data data = convert(aPosition);

    vTexCoord = (uv[data.uv] /textureRow) + offset;
    vColor = data.position;
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(data.position,1.0);
}