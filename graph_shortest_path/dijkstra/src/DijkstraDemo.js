// JavaScript
// グラフの最短経路: ダイクストラ法 (dijkstra)

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
    const edgesSet = new Set();
    for (const vertex in this._data) {
      for (const [neighbor, weight] of this._data[vertex]) {
        // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
        const edge = [vertex, neighbor].sort();
        edgesSet.add(JSON.stringify([edge[0], edge[1], weight])); // JSON文字列化して一意性を確保
      }
    }
    // セットからリストに変換して返す
    return Array.from(edgesSet).map(JSON.parse);
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
    // 既に同じ辺が存在する場合は重みを更新するかどうか？
    // 今回は単純に追加する（重複辺はプリム法では問題にならないが、データとしては綺麗でないかも）
    // ここでは、同じ頂点間の辺が存在しない場合のみ追加するように修正します。
    
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
      console.log(`ERROR: 開始頂点 '${startVertex}' または 終了頂点 '${endVertex}' がグラフに存在しません。`);
      return [null, Infinity];
    }

    // 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
    const distances = {};
    for (const vertex in this._data) {
      distances[vertex] = Infinity;
    }
    distances[startVertex] = 0;

    // 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
    const predecessors = {};
    for (const vertex in this._data) {
      predecessors[vertex] = null;
    }

    // JavaScriptには優先度キューがないので、シンプルな実装を作成する
    // これはMinHeapとして機能するシンプルな優先度キュー
    class PriorityQueue {
      constructor() {
        this.queue = [];
      }

      enqueue(element, priority) {
        this.queue.push({ element, priority });
        this.sort();
      }

      dequeue() {
        if (this.isEmpty()) return null;
        return this.queue.shift();
      }

      isEmpty() {
        return this.queue.length === 0;
      }

      sort() {
        this.queue.sort((a, b) => a.priority - b.priority);
      }
    }

    // 優先度付きキュー: (距離, 頂点) のオブジェクトを格納し、距離が小さい順に取り出す
    const priorityQueue = new PriorityQueue();
    priorityQueue.enqueue(startVertex, 0); // (開始頂点, 開始頂点への距離)

    while (!priorityQueue.isEmpty()) {
      // 優先度付きキューから最も距離の小さい頂点を取り出す
      const { element: currentVertex, priority: currentDistance } = priorityQueue.dequeue();

      // 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
      // より短い経路が既に見つかっているためスキップ
      if (currentDistance > distances[currentVertex]) {
        continue;
      }

      // 終了頂点に到達したら探索終了
      if (currentVertex === endVertex) {
        break; // 最短経路が見つかった
      }

      // 現在の頂点から到達可能な隣接頂点を探索
      // this.getNeighbors(currentVertex) は [[neighbor, weight], ...] のリストを返す
      const neighbors = this.getNeighbors(currentVertex);
      for (const [neighbor, weight] of neighbors) {
        const distanceThroughCurrent = distances[currentVertex] + weight;

        // より短い経路が見つかった場合
        if (distanceThroughCurrent < distances[neighbor]) {
          distances[neighbor] = distanceThroughCurrent;
          predecessors[neighbor] = currentVertex;
          // 優先度付きキューに隣接頂点を追加または更新
          // ダイクストラ法では heuristic は使用しない (または h=0)
          priorityQueue.enqueue(neighbor, distanceThroughCurrent);
        }
      }
    }

    // 終了頂点への最短距離が無限大のままなら、到達不可能
    if (distances[endVertex] === Infinity) {
      console.log(`INFO: 開始頂点 '${startVertex}' から 終了頂点 '${endVertex}' への経路は存在しません。`);
      return [null, Infinity];
    }

    // 最短経路を再構築
    const path = [];
    let current = endVertex;
    while (current !== null) {
      path.push(current);
      current = predecessors[current];
    }
    path.reverse(); // 経路は逆順に構築されたので反転

    // 開始ノードから開始されていることを確認
    if (path[0] !== startVertex) {
      // これが発生するのは、開始頂点が存在しないか、到達不能な場合だが、
      // 前段のチェックで大部分はカバーされているはず。
      // ここに来る場合は、特殊なケース（例えば孤立した開始点と終了点）が考えられる。
      return [null, Infinity];
    }

    return [path, distances[endVertex]];
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
  console.log("Dijkstra -----> start");

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

  console.log("\nDijkstra <----- end");
}

// JavaScriptでは直接実行可能
main();