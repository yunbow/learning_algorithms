// JavaScript
// グラフの連結成分: BFS

class GraphData {
    constructor() {
        this._data = {};
    }

    get() {
        return this._data;
    }

    getVertices() {
        return Object.keys(this._data);
    }

    getNeighbors(vertex) {
        return this._data[vertex] || null;
    }

    getVertice(vertex) {
        if (vertex in this._data) {
            return this._data[vertex];
        } else {
            console.error(`ERROR: ${vertex}は範囲外です`);
            return null;
        }
    }

    addVertex(vertex) {
        if (!(vertex in this._data)) {
            this._data[vertex] = [];
        }
        return true;
    }

    addEdge(vertex1, vertex2, weight) {
        if (!(vertex1 in this._data)) {
            this.addVertex(vertex1);
        }
        if (!(vertex2 in this._data)) {
            this.addVertex(vertex2);
        }

        const addOrUpdateEdge = (v1, v2) => {
            const existingEdgeIndex = this._data[v1].findIndex(([neighbor]) => neighbor === v2);
            if (existingEdgeIndex !== -1) {
                this._data[v1][existingEdgeIndex] = [v2, weight];
            } else {
                this._data[v1].push([v2, weight]);
            }
        };

        addOrUpdateEdge(vertex1, vertex2);
        addOrUpdateEdge(vertex2, vertex1);

        return true;
    }

    clear() {
        this._data = {};
        return true;
    }

    getConnectedComponents() {
        const visited = new Set();
        const allComponents = [];

        const vertices = this.getVertices();

        for (const vertex of vertices) {
            if (!visited.has(vertex)) {
                const currentComponent = [];
                const queue = [vertex];
                visited.add(vertex);
                currentComponent.push(vertex);

                while (queue.length > 0) {
                    const u = queue.shift();

                    const neighborsWithWeight = this.getNeighbors(u);

                    if (neighborsWithWeight) {
                        for (const [neighbor] of neighborsWithWeight) {
                            if (!visited.has(neighbor)) {
                                visited.add(neighbor);
                                queue.push(neighbor);
                                currentComponent.push(neighbor);
                            }
                        }
                    }
                }

                allComponents.push(currentComponent);
            }
        }

        return allComponents;
    }
}

function main() {
    console.log("Bfs TEST -----> start");

    console.log("\nnew");
    const graphData = new GraphData();
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

    console.log("\nadd_edge");
    graphData.clear();
    const inputList1 = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]];
    for (const input of inputList1) {
        console.log(`  入力値: ${input}`);
        const output = graphData.addEdge(...input);
        console.log(`  出力値: ${output}`);
    }
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
    console.log("\nget_connected_components");
    const output1 = graphData.getConnectedComponents();
    console.log(`  連結成分: ${JSON.stringify(output1)}`);

    console.log("\nadd_edge");
    graphData.clear();
    const inputList2 = [['A', 'B', 4], ['C', 'D', 4], ['E', 'F', 1], ['F', 'G', 1]];
    for (const input of inputList2) {
        console.log(`  入力値: ${input}`);
        const output = graphData.addEdge(...input);
        console.log(`  出力値: ${output}`);
    }
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
    console.log("\nget_connected_components");
    const output2 = graphData.getConnectedComponents();
    console.log(`  連結成分: ${JSON.stringify(output2)}`);

    console.log("\nadd_edge");
    graphData.clear();        
    const inputList3 = [['A', 'B', 4], ['B', 'C', 3], ['D', 'E', 5]];
    for (const input of inputList3) {
        console.log(`  入力値: ${input}`);
        const output = graphData.addEdge(...input);
        console.log(`  出力値: ${output}`);
    }
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
    console.log("\nget_connected_components");
    const output3 = graphData.getConnectedComponents();
    console.log(`  連結成分: ${JSON.stringify(output3)}`);

    console.log("\nadd_edge");
    graphData.clear();
    const inputList4 = [];
    for (const input of inputList4) {
        console.log(`  入力値: ${input}`);
        const output = graphData.addEdge(...input);
        console.log(`  出力値: ${output}`);
    }
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
    console.log("\nget_connected_components");
    const output4 = graphData.getConnectedComponents();
    console.log(`  連結成分: ${JSON.stringify(output4)}`);

    console.log("Bfs TEST <----- end");
}

// Uncomment the line below to run the main function
// main();