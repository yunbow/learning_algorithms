// JavaScript
// グラフの最短経路: ベルマンフォード法 (Bellman Ford)

class GraphData {
  constructor() {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
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
    // ベルマン-フォード法で使用するため、内部データ (_data) から有向辺として抽出します。
    // 各辺は [出発頂点, 到着頂点, 重み] の配列になります。
    const edges = [];
    for (const u in this._data) {
      for (const [v, weight] of this._data[u]) {
        edges.push([u, v, weight]);
      }
    }
    return edges;
  }

  addVertex(vertex) {
    // 新しい頂点をグラフに追加します。
    if (!(vertex in this._data)) {
      this._data[vertex] = [];
      return true;
    }
    return true; // 既に存在する場合は追加しないがTrueを返す
  }

  addEdge(vertex1, vertex2, weight) {
    // 両頂点間に辺を追加します。重みを指定します。
    // 頂点がグラフに存在しない場合は追加します。
    this.addVertex(vertex1);
    this.addVertex(vertex2);

    // vertex1 -> vertex2 の辺を追加（重み付き）
    // 既に同じ頂点間の辺が存在する場合は重みを更新
    let edgeUpdatedV1V2 = false;
    for (let i = 0; i < this._data[vertex1].length; i++) {
      if (this._data[vertex1][i][0] === vertex2) {
        this._data[vertex1][i] = [vertex2, weight];
        edgeUpdatedV1V2 = true;
        break;
      }
    }
    if (!edgeUpdatedV1V2) {
      this._data[vertex1].push([vertex2, weight]);
    }

    // vertex2 -> vertex1 の辺を追加（重み付き）
    // 既に同じ頂点間の辺が存在する場合は重みを更新
    let edgeUpdatedV2V1 = false;
    for (let i = 0; i < this._data[vertex2].length; i++) {
      if (this._data[vertex2][i][0] === vertex1) {
        this._data[vertex2][i] = [vertex1, weight];
        edgeUpdatedV2V1 = true;
        break;
      }
    }
    if (!edgeUpdatedV2V1) {
      this._data[vertex2].push([vertex1, weight]);
    }

    return true;
  }

  isEmpty() {
    // グラフが空かどうかを返します。
    return Object.keys(this._data).length === 0;
  }

  clear() {
    // グラフを空にします。
    this._data = {};
    return true;
  }

  getShortestPath(startVertex, endVertex, heuristic) {
    const vertices = this.getVertices();
    const edges = this.getEdges(); // 有向辺のリストを取得
    const numVertices = vertices.length;

    // 始点と終点の存在チェック
    if (!vertices.includes(startVertex)) {
      console.log(`エラー: 始点 '${startVertex}' がグラフに存在しません。`);
      return [[], Infinity];
    }
    if (!vertices.includes(endVertex)) {
      console.log(`エラー: 終点 '${endVertex}' がグラフに存在しません。`);
      return [[], Infinity];
    }

    // 始点と終点が同じ場合
    if (startVertex === endVertex) {
      return [[startVertex], 0];
    }

    // 距離と先行頂点の初期化
    // dist: 始点からの最短距離を格納
    // pred: 最短経路における各頂点の先行頂点を格納
    const dist = {};
    const pred = {};
    for (const vertex of vertices) {
      dist[vertex] = Infinity;
      pred[vertex] = null;
    }
    dist[startVertex] = 0; // 始点自身の距離は0

    // |V| - 1 回の緩和ステップを実行
    // このループの後、負閉路が存在しない場合は全ての頂点への最短距離が確定している
    for (let i = 0; i < numVertices - 1; i++) {
      // 緩和が一度も行われなかった場合にループを中断するためのフラグ
      let relaxedInThisIteration = false;
      for (const [u, v, weight] of edges) {
        // dist[u] が無限大でない場合のみ緩和を試みる（到達不可能な頂点からの緩和は意味がない）
        if (dist[u] !== Infinity && dist[u] + weight < dist[v]) {
          dist[v] = dist[u] + weight;
          pred[v] = u;
          relaxedInThisIteration = true;
        }
      }
      // このイテレーションで緩和が行われなかった場合は、それ以上距離が更新されることはないのでループを抜ける
      if (!relaxedInThisIteration) {
        break;
      }
    }

    // 負閉路の検出
    // もう一度全ての辺に対して緩和を試みる。
    // ここでさらに距離が更新される辺が存在する場合、その辺は負閉路の一部であるか、
    // 負閉路から到達可能な頂点への辺である。
    // 終点が負閉路から到達可能な場合、終点までの最短距離は無限小になるため定義できない。
    for (const [u, v, weight] of edges) {
      if (dist[u] !== Infinity && dist[u] + weight < dist[v]) {
        // 負閉路が存在します。
        // 終点がこの負閉路から到達可能であれば、最短経路は存在しません。
        // 厳密には終点への到達可能性をチェックすべきですが、ベルマン・フォード法では負閉路自体の検出をもって最短経路定義不可とすることが一般的です。
        console.log("エラー: グラフに負閉路が存在します。最短経路は定義できません。");
        return [null, -Infinity]; // 負の無限大を返すことで、距離が無限小になることを示す
      }
    }

    // 最短経路の構築
    const path = [];
    let current = endVertex;

    // 終点まで到達不可能かチェック (距離が初期値のままか)
    if (dist[endVertex] === Infinity) {
      return [[], Infinity]; // 到達不可能
    }

    // 終点から先行頂点をたどって経路を逆順に構築
    while (current !== null) {
      path.push(current);
      // 始点に到達したらループを終了
      if (current === startVertex) {
        break;
      }
      // 次の頂点に進む
      current = pred[current];
    }

    // 経路が始点から始まっていない場合 (通常は到達不可能な場合に含まれるはずだが、念のため)
    // pathリストの最後の要素がstartVertexであることを確認
    if (path.length === 0 || path[path.length - 1] !== startVertex) {
      // このケースは dist[endVertex] === Infinity で既に処理されているはずだが、念のため
      // または、負閉路検出後に到達不可能と判断されるケースもありうる
      return [[], Infinity];
    }

    path.reverse(); // 経路を始点から終点の順にする

    return [path, dist[endVertex]];
  }
}

// ヒューリスティック関数 (ベルマン-フォード法では使用しないが、元のコードに合わせた引数のために残す)
function dummyHeuristic(u, v) {
  // u と v の間に何らかの推定距離を計算する関数
  // ここではベルマン-フォード法では使用しないため、常に0を返す
  return 0;
}

function main() {
  console.log("BellmanFord TEST -----> start");

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

  console.log("\nBellmanFord TEST <----- end");
}

// Node.js環境で実行する場合
if (typeof require !== 'undefined' && require.main === module) {
  main();
}
// ブラウザ環境で実行する場合にもエクスポート
if (typeof module !== 'undefined') {
  module.exports = { GraphData, dummyHeuristic, main };
}