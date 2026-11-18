#version 330

// GeoCore light-map additions: the cell's addition, blended additively onto vanilla's light map.
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    fragColor = vertexColor;
}
