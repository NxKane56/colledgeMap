uniform mat4 u_ModelViewProjectionMatrix;

attribute vec3 a_vertex;
attribute vec3 a_normal;
varying vec3 v_vertex;
varying vec3 v_normal;


void main() {
    v_vertex=a_vertex;
    vec3 n_normal=normalize(a_normal);
    v_normal=n_normal;
    gl_Position = u_ModelViewProjectionMatrix * vec4(a_vertex, 1.0);
}
