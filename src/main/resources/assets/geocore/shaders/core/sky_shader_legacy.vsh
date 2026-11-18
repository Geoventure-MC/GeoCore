#version 150

// GeoCore Sky.shaderQuads before 26.1: the vertex half of a mod's sky shader (see sky_shader.vsh). GeoCoreSkyLocal is the
// sky pose at the time of the call; the program JSON is written by fr.geoventure.geocore.render.SkyShaders.

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat4 GeoCoreSkyLocal;

in vec3 Position;
in vec2 UV0;

out vec2 texCoord0;
out vec3 localPos;

void main() {
    localPos = Position;
    texCoord0 = UV0;
    gl_Position = ProjMat * ModelViewMat * GeoCoreSkyLocal * vec4(Position, 1.0);
}
