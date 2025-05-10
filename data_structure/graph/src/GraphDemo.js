// JavaScript
// データ構造: グラフ (Graph)

class GraphData {
    constructor() {
        // 隣接ノードとその辺の重みを格納します。
        // キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
        // 例: { 'A': [['B', 1], ['C', 4]], 'B': [['A', 1], ['C', 2], ['D', 5]], ... }
        this._data = {};
    }

    get() {
        // グラフの内部データを取得します。
        return this._data;
    }

    getVertices() {
        // グラフの全頂点を配列として返します。
        return Object.keys(this._data);
    }
    
    getEdges() {
        // グラフの全辺を配列として返します。
        // 無向グラフの場合、[u, v, weight] の形式で返します。
        // 重複を避けるためにセットを使用します。
        const edges = new Set();
        for (const vertex in this._data) {
            for (const [neighbor, weight] of this._data[vertex]) {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                const edge = [vertex, neighbor].sort();
                edges.add(JSON.stringify([edge[0], edge[1], weight])); // JSON文字列化してセットに格納
            }
        }
        // JSON文字列をオブジェクトに戻して返す
        return Array.from(edges).map(edge => JSON.parse(edge));
    }
    
    getNeighbors(vertex) {
        // 指定された頂点の隣接ノードと辺の重みの配列を返します。
        // 形式: [[隣接頂点, 重み], ...]
        if (vertex in this._data) {
            return this._data[vertex];
        } else {
            return null; // 頂点が存在しない場合はNullを返す
        }
    }

    getEdgeWeight(vertex1, vertex2) {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はNullを返します。
        if (vertex1 in this._data && vertex2 in this._data) {
            for (const [neighbor, weight] of this._data[vertex1]) {
                if (neighbor === vertex2) {
                    return weight;
                }
            }
        }
        return null; // 辺が存在しない場合
    }

    getVertice(vertex) {
        // 頂点がグラフに存在するか確認する
        if (vertex in this._data) {
            // 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            return this._data[vertex];
        } else {
            // 存在しない場合はメッセージを表示し、Nullを返す
            console.log(`ERROR: ${vertex}は範囲外です`);
            return null;
        }
    }

    getEdge(vertex1, vertex2) {
        // 指定された2つの頂点間に辺が存在するかを確認する
        // 両方の頂点がグラフに存在する必要がある
        if (vertex1 in this._data && vertex2 in this._data) {
            // vertex1の隣接リストにvertex2が含まれているかを確認
            // 無向グラフなので、片方を確認すれば十分
            return this._data[vertex1].some(([neighbor, _]) => neighbor === vertex2);
        } else {
            // どちらかの頂点が存在しない場合は辺も存在しない
            return false;
        }
    }

    addVertex(vertex) {
        // 新しい頂点をグラフに追加します。
        if (!(vertex in this._data)) {
            this._data[vertex] = [];
            return true;
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true;
    }
    
    addEdge(vertex1, vertex2, weight) {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if (!(vertex1 in this._data)) {
            this.addVertex(vertex1);
        }
        if (!(vertex2 in this._data)) {
            this.addVertex(vertex2);
        }
        
        // 両方向に辺を追加する（無向グラフ）
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        let edgeExistsV1V2 = false;
        for (let i = 0; i < this._data[vertex1].length; i++) {
            if (this._data[vertex1][i][0] === vertex2) {
                this._data[vertex1][i] = [vertex2, weight]; // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true;
                break;
            }
        }
        if (!edgeExistsV1V2) {
            this._data[vertex1].push([vertex2, weight]);
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        let edgeExistsV2V1 = false;
        for (let i = 0; i < this._data[vertex2].length; i++) {
            if (this._data[vertex2][i][0] === vertex1) {
                this._data[vertex2][i] = [vertex1, weight]; // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true;
                break;
            }
        }
        if (!edgeExistsV2V1) {
            this._data[vertex2].push([vertex1, weight]);
        }
        
        return true;
    }
    
    removeVertex(vertex) {
        // 頂点とそれに関連する辺を削除する
        if (vertex in this._data) {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for (const v in this._data) {
                this._data[v] = this._data[v].filter(([neighbor, _]) => neighbor !== vertex);
            }
            // 頂点自体を削除する
            delete this._data[vertex];
            return true;
        } else {
            console.log(`ERROR: ${vertex} は範囲外です`);
            return false;
        }
    }

    removeEdge(vertex1, vertex2) {
        // 両頂点間の辺を削除します。
        if (vertex1 in this._data && vertex2 in this._data) {
            let removed = false;
            // vertex1 から vertex2 への辺を削除
            const originalLenV1 = this._data[vertex1].length;
            this._data[vertex1] = this._data[vertex1].filter(([neighbor, _]) => neighbor !== vertex2);
            if (this._data[vertex1].length < originalLenV1) {
                removed = true;
            }

            // vertex2 から vertex1 への辺を削除
            const originalLenV2 = this._data[vertex2].length;
            this._data[vertex2] = this._data[vertex2].filter(([neighbor, _]) => neighbor !== vertex1);
            if (this._data[vertex2].length < originalLenV2) {
                removed = true;
            }
                
            return removed; // 少なくとも片方向が削除されたか
        } else {
            console.log(`ERROR: ${vertex1} または ${vertex2} は範囲外です`);
            return false;
        }
    }

    isEmpty() {
        // グラフが空かどうか
        return Object.keys(this._data).length === 0;
    }
    
    size() {
        // グラフの頂点数を返す
        return Object.keys(this._data).length;
    }
    
    clear() {
        // グラフを空にする
        this._data = {};
        return true;
    }
}

function main() {
    console.log("Graph TEST -----> start");

    console.log("\nnew");
    const graphData = new GraphData();
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

    console.log("\nis_empty");
    let output = graphData.isEmpty();
    console.log(`  出力値: ${output}`);

    console.log("\nsize");
    output = graphData.size();
    console.log(`  出力値: ${output}`);

    const inputList = ['A', 'B', 'C'];
    for (const input of inputList) {
        console.log("\nadd_vertex");
        console.log(`  入力値: ${input}`);
        output = graphData.addVertex(input);
        console.log(`  出力値: ${output}`);
    }
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

    console.log("\nget_vertices");
    output = graphData.getVertices();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nsize");
    output = graphData.size();
    console.log(`  出力値: ${output}`);

    console.log("\nadd_edge");
    const edgeInputList = [['A', 'B', 4], ['B', 'C', 2], ['C', 'A', 3]];
    for (const input of edgeInputList) {
        console.log(`  入力値: ${JSON.stringify(input)}`);
        output = graphData.addEdge(...input);
        console.log(`  出力値: ${output}`);
    }
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

    console.log("\nget_vertices");
    output = graphData.getVertices();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nget_edges");
    output = graphData.getEdges();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nsize");
    output = graphData.size();
    console.log(`  出力値: ${output}`);

    console.log("\nget_vertice");
    let vertexInput = 'B';
    console.log(`  入力値: '${vertexInput}'`);
    output = graphData.getVertice(vertexInput);
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nget_vertice");
    vertexInput = 'E';
    console.log(`  入力値: '${vertexInput}'`);
    output = graphData.getVertice(vertexInput);
    console.log(`  出力値: ${output}`);

    console.log("\nremove_edge");
    let edgeInput = ['A', 'B'];
    console.log(`  入力値: ${JSON.stringify(edgeInput)}`);
    output = graphData.removeEdge(...edgeInput);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

    console.log("\nremove_edge");
    edgeInput = ['A', 'C'];
    console.log(`  入力値: ${JSON.stringify(edgeInput)}`);
    output = graphData.removeEdge(...edgeInput);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

    console.log("\nget_edges");
    output = graphData.getEdges();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nremove_vertex");
    vertexInput = 'B';
    console.log(`  入力値: ${vertexInput}`);
    output = graphData.removeVertex(vertexInput);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

    console.log("\nremove_vertex");
    vertexInput = 'Z';
    console.log(`  入力値: ${vertexInput}`);
    output = graphData.removeVertex(vertexInput);
    console.log(`  出力値: ${output}`);
    console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

    console.log("\nget_vertices");
    output = graphData.getVertices();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nget_edges");
    output = graphData.getEdges();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nsize");
    output = graphData.size();
    console.log(`  出力値: ${output}`);

    console.log("\nget_vertice");
    vertexInput = 'B';
    console.log(`  入力値: ${vertexInput}`);
    output = graphData.getVertice(vertexInput);
    console.log(`  出力値: ${output}`);

    console.log("\nclear");
    output = graphData.clear();
    console.log(`  出力値: ${output}`);

    console.log("\nis_empty");
    output = graphData.isEmpty();
    console.log(`  出力値: ${output}`);

    console.log("\nsize");
    output = graphData.size();
    console.log(`  出力値: ${output}`);

    console.log("\nget_vertices");
    output = graphData.getVertices();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nget_edges");
    output = graphData.getEdges();
    console.log(`  出力値: ${JSON.stringify(output)}`);

    console.log("\nGraph TEST <----- end");
}

// Node.jsで実行する場合はコメントを外してください
// if (require.main === module) {
//     main();
// }

// ブラウザで実行する場合
// main();