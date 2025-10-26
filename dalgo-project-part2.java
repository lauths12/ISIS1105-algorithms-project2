import java.util.*;

//Juan David Rios Nisperuza 202215787
//Laura Julieth Carretero Serrano 202214922 

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int testCases = scanner.nextInt();
        for (int i = 0; i < testCases; i++) {
            int m = scanner.nextInt();
            int n = scanner.nextInt();
            int k = scanner.nextInt();

            int[][] matrix = new int[m][n];
            for (int j = 0; j < m; j++) {
                for (int l = 0; l < n; l++) {
                    matrix[j][l] = scanner.nextInt();
                }
            }

            solve(matrix, m, n, k);
        }


        scanner.close();
    }

    static void solve(int[][] matrix, int m, int n, int k) {
        Map<Integer, Map<String, int[]>> esquinasPorValor = encontrarEsquinasSubmatrices(matrix);
        Map<Integer, Set<Integer>> valoresEncerrados = obtenerValoresEncerrados(matrix, esquinasPorValor);
        Map<Integer, Set<Integer>> grafo = valoresEncerrados;
        List<Integer> ordenTopologico = ordenamientoTopologico(grafo);

        if (ordenTopologico == null) {
            System.out.println("NO SE PUEDE");
        } else {
            for (int valor : ordenTopologico) {
                Map<String, int[]> esquinas = esquinasPorValor.get(valor);
                int[] superiorIzquierda = esquinas.get("superior_izquierda");
                int[] inferiorDerecha = esquinas.get("inferior_derecha");
                System.out.println(valor + " " + (superiorIzquierda[0] + 1) + " " + (inferiorDerecha[0] + 1) + " " + (superiorIzquierda[1] + 1) + " " + (inferiorDerecha[1] + 1));
            }
        }
    }

    static Map<Integer, Map<String, int[]>> encontrarEsquinasSubmatrices(int[][] matriz) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        Map<Integer, Map<String, int[]>> esquinasPorValor = new HashMap<>();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int valor = matriz[i][j];
                if (!esquinasPorValor.containsKey(valor)) {
                    Map<String, int[]> esquinas = new HashMap<>();
                    int[] superiorIzquierda = {i, j};
                    int[] inferiorDerecha = {i, j};
                    esquinas.put("superior_izquierda", superiorIzquierda);
                    esquinas.put("inferior_derecha", inferiorDerecha);
                    esquinasPorValor.put(valor, esquinas);
                } else {
                    int[] superiorIzquierda = esquinasPorValor.get(valor).get("superior_izquierda");
                    int[] inferiorDerecha = esquinasPorValor.get(valor).get("inferior_derecha");
                    superiorIzquierda[0] = Math.min(superiorIzquierda[0], i);
                    superiorIzquierda[1] = Math.min(superiorIzquierda[1], j);
                    inferiorDerecha[0] = Math.max(inferiorDerecha[0], i);
                    inferiorDerecha[1] = Math.max(inferiorDerecha[1], j);
                }
            }
        }

        return esquinasPorValor;
    }

    static Map<Integer, Set<Integer>> obtenerValoresEncerrados(int[][] matriz, Map<Integer, Map<String, int[]>> esquinasPorValor) {
        Map<Integer, Set<Integer>> valoresEncerrados = new HashMap<>();

        for (Map.Entry<Integer, Map<String, int[]>> entry : esquinasPorValor.entrySet()) {
            int valor = entry.getKey();
            Map<String, int[]> esquinas = entry.getValue();
            int filaInicio = esquinas.get("superior_izquierda")[0];
            int columnaInicio = esquinas.get("superior_izquierda")[1];
            int filaFin = esquinas.get("inferior_derecha")[0];
            int columnaFin = esquinas.get("inferior_derecha")[1];

            Set<Integer> valores = new HashSet<>();
            valoresEncerrados.put(valor, valores);

            for (int i = filaInicio; i <= filaFin; i++) {
                for (int j = columnaInicio; j <= columnaFin; j++) {
                    if (matriz[i][j] == valor) {
                        if (i + 1 < filaFin + 1 && matriz[i + 1][j] != valor) {
                            valores.add(matriz[i + 1][j]);
                        }
                        if (i - 1 > filaInicio - 1 && matriz[i - 1][j] != valor) {
                            valores.add(matriz[i - 1][j]);
                        }
                        if (j + 1 < columnaFin + 1 && matriz[i][j + 1] != valor) {
                            valores.add(matriz[i][j + 1]);
                        }
                        if (j - 1 > columnaInicio - 1 && matriz[i][j - 1] != valor) {
                            valores.add(matriz[i][j - 1]);
                        }
                    }
                }
            }
        }

        return valoresEncerrados;
    }

    static List<Integer> ordenamientoTopologico(Map<Integer, Set<Integer>> grafo) {
        Map<Integer, Integer> gradosEntrada = new HashMap<>();
        for (int nodo : grafo.keySet()) {
            gradosEntrada.put(nodo, 0);
        }
        for (Set<Integer> nodosDestino : grafo.values()) {
            for (int nodoDestino : nodosDestino) {
                gradosEntrada.put(nodoDestino, gradosEntrada.get(nodoDestino) + 1);
            }
        }

        Queue<Integer> cola = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : gradosEntrada.entrySet()) {
            if (entry.getValue() == 0) {
                cola.add(entry.getKey());
            }
        }

        List<Integer> ordenTopologico = new ArrayList<>();

        while (!cola.isEmpty()) {
            int nodoActual = cola.poll();
            ordenTopologico.add(nodoActual);

            for (int nodoDestino : grafo.getOrDefault(nodoActual, new HashSet<>())) {
                gradosEntrada.put(nodoDestino, gradosEntrada.get(nodoDestino) - 1);
                if (gradosEntrada.get(nodoDestino) == 0) {
                    cola.add(nodoDestino);
                }
            }
        }

        if (ordenTopologico.size() != grafo.size()) {
            return null;
        }

        return ordenTopologico;

    }

}
