// JavaScript
// グラフの最短経路: A-star

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
                edges.add(JSON.stringify([edge[0], edge[1], weight])); // JSONを使用して配列を文字列化
            }
        }
        // 文字列化された配列を元の形式に戻す
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

    clear() {
        // グラフを空にします。
        this._data = {};
        return true;
    }

    getShortestPath(startVertex, endVertex, heuristic) {
        if (!(startVertex in this._data) || !(endVertex in this._data)) {
            console.log("ERROR: 開始頂点または終了頂点がグラフに存在しません。");
            return [null, Infinity];
        }

        if (startVertex === endVertex) {
            return [[startVertex], 0];
        }

        // g_costs: 開始ノードから各ノードまでの既知の最短コスト
        const gCosts = {};
        for (const vertex in this._data) {
            gCosts[vertex] = Infinity;
        }
        gCosts[startVertex] = 0;

        // f_costs: g_costs + ヒューリスティックコスト (推定合計コスト)
        const fCosts = {};
        for (const vertex in this._data) {
            fCosts[vertex] = Infinity;
        }
        fCosts[startVertex] = heuristic(startVertex, endVertex);

        // came_from: 最短経路で各ノードの直前のノードを記録
        const cameFrom = {};

        // JavaScriptにはネイティブの優先度キューがないため、
        // 単純な配列と手動でのソートを使用します。
        // open_set: 探索すべきノードの優先度キュー [f_cost, node]
        let openSet = [[fCosts[startVertex], startVertex]];

        while (openSet.length > 0) {
            // open_set から最も f_cost が低いノードを取り出す
            openSet.sort((a, b) => a[0] - b[0]);
            const [currentFCost, currentVertex] = openSet.shift();

            // 取り出したノードの f_cost が、記録されている f_costs[current_vertex] より大きい場合、
            // それは古い情報なので無視して次のノードに進む
            if (currentFCost > fCosts[currentVertex]) {
                 continue;
            }

            // 目標ノードに到達した場合、経路を再構築して返す
            if (currentVertex === endVertex) {
                return [this._reconstructPath(cameFrom, endVertex), gCosts[endVertex]];
            }

            // 現在のノードの隣接ノードを調べる
            const neighbors = this.getNeighbors(currentVertex);
            if (neighbors === null) { // 孤立したノードの場合など
                continue;
            }

            for (const [neighbor, weight] of neighbors) {
                // 現在のノードを経由した場合の隣接ノードへの新しい g_cost
                const tentativeGCost = gCosts[currentVertex] + weight;

                // 新しい g_cost が、現在記録されている隣接ノードへの g_cost よりも小さい場合
                if (tentativeGCost < gCosts[neighbor]) {
                    // 経路情報を更新
                    cameFrom[neighbor] = currentVertex;
                    gCosts[neighbor] = tentativeGCost;
                    fCosts[neighbor] = gCosts[neighbor] + heuristic(neighbor, endVertex);
                    
                    // 隣接ノードを open_set に追加（または優先度を更新）
                    openSet.push([fCosts[neighbor], neighbor]);
                }
            }
        }

        // open_set が空になっても目標ノードに到達しなかった場合、経路は存在しない
        return [null, Infinity];
    }

    _reconstructPath(cameFrom, currentVertex) {
        const path = [];
        while (currentVertex in cameFrom) {
            path.push(currentVertex);
            currentVertex = cameFrom[currentVertex];
        }
        path.push(currentVertex); // 開始ノードを追加
        return path.reverse(); // 経路を逆順にする (開始 -> 目標)
    }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
// 実際のA*では、問題に応じた適切な推定関数を使用する必要があります。
function dummyHeuristic(u, v) {
    // u と v の間に何らかの推定距離を計算する関数
    // ここではダミーとして常に0を返す
    return 0;
}

function main() {
    console.log("A-start TEST -----> start");

    const graphData = new GraphData();

    graphData.clear();
    const inputList1 = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]];
    for (const input of inputList1) {
        graphData.addEdge(...input);
    }
    console.log("\nグラフの頂点:", graphData.getVertices());
    console.log("グラフの辺 (重み付き):", graphData.getEdges());
    const input1 = ['A', 'B'];
    const shortestPath1 = graphData.getShortestPath(input1[0], input1[1], dummyHeuristic);
    console.log(`経路${input1[0]}-${input1[1]} の最短経路は ${shortestPath1[0]} (重み: ${shortestPath1[1]})`);

    graphData.clear();
    const inputList2 = [['A', 'B', 4], ['C', 'D', 4], ['E', 'F', 1], ['F', 'G', 1]];
    for (const input of inputList2) {
        graphData.addEdge(...input);
    }
    console.log("\nグラフの頂点:", graphData.getVertices());
    console.log("グラフの辺 (重み付き):", graphData.getEdges());
    const input2 = ['A', 'B'];
    const shortestPath2 = graphData.getShortestPath(input2[0], input2[1], dummyHeuristic);
    console.log(`経路${input2[0]}-${input2[1]} の最短経路は ${shortestPath2[0]} (重み: ${shortestPath2[1]})`);

    graphData.clear();
    const inputList3 = [['A', 'B', 4], ['B', 'C', 3], ['D', 'E', 5]];
    for (const input of inputList3) {
        graphData.addEdge(...input);
    }
    console.log("\nグラフの頂点:", graphData.getVertices());
    console.log("グラフの辺 (重み付き):", graphData.getEdges());
    const input3 = ['A', 'D'];
    const shortestPath3 = graphData.getShortestPath(input3[0], input3[1], dummyHeuristic);
    console.log(`経路${input3[0]}-${input3[1]} の最短経路は ${shortestPath3[0]} (重み: ${shortestPath3[1]})`);

    graphData.clear();
    const inputList4 = [];
    for (const input of inputList4) {
        graphData.addEdge(...input);
    }
    console.log("\nグラフの頂点:", graphData.getVertices());
    console.log("グラフの辺 (重み付き):", graphData.getEdges());
    const input4 = ['A', 'B'];
    const shortestPath4 = graphData.getShortestPath(input4[0], input4[1], dummyHeuristic);
    console.log(`経路${input4[0]}-${input4[1]} の最短経路は ${shortestPath4[0]} (重み: ${shortestPath4[1]})`);

    console.log("\nA-start TEST <----- end");
}

main();