#version 150

// GeoCore Sky.shaderQuads, 26.1: the vertex half of a mod's sky shader. Positions arrive as submitted (1.7 gl_Vertex)
// and are posed by GeoCoreSkyLocal, the sky pose at the time of the call. A mod's fragment shader reads texCoord0
// (1.7 gl_TexCoord[0].xy) and localPos (the submitted position, geontm-space default.vert's vPosition).
// The two vanilla blocks are copies of dynamictransforms.glsl and projection.glsl (no #moj_import, so the file stays
// at GLSL 1.50 like the fragment shaders it is linked with).

layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
    mat4 TextureMat;
};
layout(std140) uniform Projection {
    mat4 ProjMat;
};
layout(std140) uniform GeoCoreSkyShader {
    mat4 GeoCoreSkyLocal;
    vec4 GeoCoreSkyParams[16];
};

in vec3 Position;
in vec2 UV0;

out vec2 texCoord0;
out vec3 localPos;

void main() {
    localPos = Position;
    texCoord0 = UV0;
    gl_Position = ProjMat * ModelViewMat * GeoCoreSkyLocal * vec4(Position, 1.0);
}
