#version 330

// GeoCore light-map additions (fr.geoventure.geocore.client.Lightmaps): a light-map cell in normalised device coordinates.
in vec3 Position;
in vec4 Color;

out vec4 vertexColor;

void main() {
    gl_Position = vec4(Position, 1.0);
    vertexColor = Color;
}
