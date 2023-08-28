#version 330
//#define byte_size 0xFFu

layout(location=0)in uint aVertexData;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec3 vPosition;
out vec2 vTexCoord;


struct Vertex_Data
{
    vec3 position;
    uint uv;
};

//https://stackoverflow.com/questions/14081024/opengl-using-a-logarithmic-coordinates-axis
float log_ten(float x){
    return (1.0 / log(10.0)) * log(x);
}

float num_to_decimal(uint data)
{
    bool negative_bit = ((data >> 7) & 0x1u) == 0x1u;
    float whole_number = float((data >> 4) & 0x7u);
    float decimal_place = float(data & 0xFu);
    float decimal_place_float = 0.0;
    if(decimal_place != 0.0)
    {
        float tenth_power = floor(log_ten(decimal_place));
        float denominator = 10.0 * pow(10.0,tenth_power);
        decimal_place_float = (1.0 / denominator) * decimal_place;
    }
    return (negative_bit ? -1.0 : 1.0) * (whole_number + decimal_place_float);
}

Vertex_Data convert(uint data)
{
    Vertex_Data vertexData;
    vertexData.position.x = num_to_decimal((data >> 18) & 0xFFu);
    vertexData.position.y = num_to_decimal((data >> 10) & 0xFFu);
    vertexData.position.z = num_to_decimal((data >> 2) & 0xFFu);
    vertexData.uv = data & 0x3u;
    return vertexData;
}


vec2 uv[4]= vec2[4](
vec2(0.0,0.0),
vec2(0.0,1.0),
vec2(1.0,0.0),
vec2(1.0,1.0)
);

void main()
{
    Vertex_Data vertexData = convert(aVertexData);

    vTexCoord = uv[vertexData.uv];
    vPosition = vertexData.position;
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertexData.position, 1.0);
}