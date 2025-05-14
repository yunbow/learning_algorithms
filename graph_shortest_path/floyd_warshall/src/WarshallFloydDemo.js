// JavaScript
// グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

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
    // グラフの全頂点をリストとして返します。
    return Object.keys(this._data);
  }

  getEdges() {
    // グラフの全辺をリストとして返します。
    // 無向グラフの場合、[u, v, weight] の形式で返します。
    // 重複を避けるためにセットを使用します。
    const edges = new Set();
    for (const vertex in this._data) {
      for (const [neighbor, weight] of this._data[vertex]) {
        // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
        const edge = [vertex, neighbor].sort();
        edges.add(JSON.stringify([edge[0], edge[1], weight])); // 配列をセットで使うためJSON文字列化
      }
    }
    // JSON文字列をパースして戻す
    return Array.from(edges).map(edge => JSON.parse(edge));
  }

  getNeighbors(vertex) {
    // 指定された頂点の隣接ノードと辺の重みのリストを返します。
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
    const vertices = this.getVertices();
    const numVertices = vertices.length;
    if (numVertices === 0) {
      return [null, Infinity];
    }

    // 頂点名をインデックスにマッピング
    const vertexToIndex = {};
    const indexToVertex = {};
    vertices.forEach((vertex, index) => {
      vertexToIndex[vertex] = index;
      indexToVertex[index] = vertex;
    });

    // 開始・終了頂点が存在するか確認
    if (!(startVertex in vertexToIndex) || !(endVertex in vertexToIndex)) {
      console.log(`ERROR: ${startVertex} または ${endVertex} がグラフに存在しません。`);
      return [null, Infinity];
    }

    const startIndex = vertexToIndex[startVertex];
    const endIndex = vertexToIndex[endVertex];

    // 距離行列 (dist) と経路復元用行列 (nextNode) を初期化
    const INF = Infinity;
    const dist = Array(numVertices).fill().map(() => Array(numVertices).fill(INF));
    const nextNode = Array(numVertices).fill().map(() => Array(numVertices).fill(null));

    // 初期距離と経路復元情報を設定
    for (let i = 0; i < numVertices; i++) {
      dist[i][i] = 0; // 自分自身への距離は0
      const neighbors = this.getNeighbors(vertices[i]) || [];
      for (const [neighbor, weight] of neighbors) {
        const j = vertexToIndex[neighbor];
        dist[i][j] = weight;
        nextNode[i][j] = j; // iからjへの直接辺の場合、iの次はj
      }
    }

    // ワーシャル-フロイド法の本体
    // k: 中継点として使用する頂点のインデックス
    for (let k = 0; k < numVertices; k++) {
      // i: 開始頂点のインデックス
      for (let i = 0; i < numVertices; i++) {
        // j: 終了頂点のインデックス
        for (let j = 0; j < numVertices; j++) {
          // i -> k -> j の経路が i -> j の現在の経路より短い場合
          if (dist[i][k] !== INF && dist[k][j] !== INF && dist[i][k] + dist[k][j] < dist[i][j]) {
            dist[i][j] = dist[i][k] + dist[k][j];
            nextNode[i][j] = nextNode[i][k]; // iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点
          }
        }
      }
    }

    // 指定された開始・終了頂点間の最短経路と重みを取得
    const shortestDistance = dist[startIndex][endIndex];

    // 経路が存在しない場合 (距離がINF)
    if (shortestDistance === INF) {
      return [null, INF];
    }

    // 経路を復元
    let path = [];
    let u = startIndex;
    // 開始と終了が同じ場合は経路は開始頂点のみ
    if (u === endIndex) {
      path = [startVertex];
    } else {
      // nextNodeを使って経路をたどる
      while (u !== null && u !== endIndex) {
        path.push(indexToVertex[u]);
        u = nextNode[u][endIndex];
        // 無限ループ防止のための簡易チェック
        if (u !== null && indexToVertex[u] === path[path.length - 1]) {
          // 同じ頂点に戻ってきたなど、異常な経路復元を防ぐ
          console.log(`WARNING: 経路復元中に異常を検出しました（${indexToVertex[u]}でループ？）。`);
          path = null;
          shortestDistance = INF;
          break;
        }
      }
      // 最後のノード (endVertex) を追加
      if (path !== null) {
        path.push(endVertex);
      }
    }

    return [path, shortestDistance];
  }
}

// ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
function dummyHeuristic(u, v) {
  // u と v の間に何らかの推定距離を計算する関数
  // ここではダミーとして常に0を返す
  return 0;
}

function main() {
  console.log("WarshallFloyd -----> start");

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

  console.log("\nWarshallFloyd <----- end");
}

main();