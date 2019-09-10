precision mediump float;

uniform vec3 u_lightPosition;
varying vec3 v_vertex;
varying vec3 v_normal;

void main() {
    vec3 n_normal=normalize(v_normal);
    vec3 light_vector = normalize(u_lightPosition - v_vertex);
    float ambient=0.3;
    float k_diffuse=0.8;
    float diffuse = k_diffuse * max(dot(n_normal, light_vector), 0.0);
    vec4 light = vec4(1.0, 1.0, 1.0, 1.0);
    gl_FragColor = (ambient+diffuse)*light;
}