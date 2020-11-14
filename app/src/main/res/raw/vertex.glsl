attribute vec3 in_Position;
attribute vec4 in_Color;

varying vec4 v_Color;

uniform mat4 vp_matrix;

void main() {
    v_Color = in_Color;
    gl_Position = vp_matrix * vec4(in_Position, 1);
}