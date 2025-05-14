// C#
// グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

using System;
using System.Collections.Generic;
using System.Linq;

namespace WarshallFloydDemo
{
    // グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)
    public class GraphData
    {
        // 隣接ノードとその辺の重みを格納します。
        // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
        private Dictionary<string, List<Tuple<string, double>>> _data;

        public GraphData()
        {
            _data = new Dictionary<string, List<Tuple<string, double>>>();
        }

        public Dictionary<string, List<Tuple<string, double>>> Get()
        {
            // グラフの内部データを取得します。
            return _data;
        }

        public List<string> GetVertices()
        {
            // グラフの全頂点をリストとして返します。
            return _data.Keys.ToList();
        }

        public List<Tuple<string, string, double>> GetEdges()
        {
            // グラフの全辺をリストとして返します。
            // 無向グラフの場合、(u, v, weight) の形式で返します。
            // 重複を避けるためにHashSetを使用します。
            HashSet<Tuple<string, string, double>> edges = new HashSet<Tuple<string, string, double>>();
            foreach (var vertex in _data.Keys)
            {
                foreach (var neighbor in _data[vertex])
                {
                    // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                    var sortedVertices = new[] { vertex, neighbor.Item1 }.OrderBy(v => v).ToArray();
                    edges.Add(new Tuple<string, string, double>(sortedVertices[0], sortedVertices[1], neighbor.Item2));
                }
            }
            return edges.ToList();
        }

        public List<Tuple<string, double>> GetNeighbors(string vertex)
        {
            // 指定された頂点の隣接ノードと辺の重みのリストを返します。
            // 形式: [(隣接頂点, 重み), ...]
            if (_data.ContainsKey(vertex))
            {
                return _data[vertex];
            }
            else
            {
                return null; // 頂点が存在しない場合はNullを返す
            }
        }

        public bool AddVertex(string vertex)
        {
            // 新しい頂点をグラフに追加します。
            if (!_data.ContainsKey(vertex))
            {
                _data[vertex] = new List<Tuple<string, double>>();
                return true;
            }
            // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
            return true;
        }

        public bool AddEdge(string vertex1, string vertex2, double weight)
        {
            // 両頂点間に辺を追加します。重みを指定します。
            // 頂点がグラフに存在しない場合は追加します。
            if (!_data.ContainsKey(vertex1))
            {
                AddVertex(vertex1);
            }
            if (!_data.ContainsKey(vertex2))
            {
                AddVertex(vertex2);
            }

            // 両方向に辺を追加する（無向グラフ）

            // vertex1 -> vertex2 の辺を追加（重み付き）
            bool edgeExistsV1V2 = false;
            for (int i = 0; i < _data[vertex1].Count; i++)
            {
                if (_data[vertex1][i].Item1 == vertex2)
                {
                    _data[vertex1][i] = new Tuple<string, double>(vertex2, weight); // 既に存在する場合は重みを更新
                    edgeExistsV1V2 = true;
                    break;
                }
            }
            if (!edgeExistsV1V2)
            {
                _data[vertex1].Add(new Tuple<string, double>(vertex2, weight));
            }

            // vertex2 -> vertex1 の辺を追加（重み付き）
            bool edgeExistsV2V1 = false;
            for (int i = 0; i < _data[vertex2].Count; i++)
            {
                if (_data[vertex2][i].Item1 == vertex1)
                {
                    _data[vertex2][i] = new Tuple<string, double>(vertex1, weight); // 既に存在する場合は重みを更新
                    edgeExistsV2V1 = true;
                    break;
                }
            }
            if (!edgeExistsV2V1)
            {
                _data[vertex2].Add(new Tuple<string, double>(vertex1, weight));
            }

            return true;
        }

        public bool Clear()
        {
            // グラフを空にします。
            _data.Clear();
            return true;
        }

        public Tuple<List<string>, double> GetShortestPath(string startVertex, string endVertex, Func<string, string, double> heuristic)
        {
            List<string> vertices = GetVertices();
            int numVertices = vertices.Count;
            if (numVertices == 0)
            {
                return new Tuple<List<string>, double>(null, double.PositiveInfinity);
            }

            // 頂点名をインデックスにマッピング
            Dictionary<string, int> vertexToIndex = new Dictionary<string, int>();
            Dictionary<int, string> indexToVertex = new Dictionary<int, string>();
            for (int i = 0; i < vertices.Count; i++)
            {
                vertexToIndex[vertices[i]] = i;
                indexToVertex[i] = vertices[i];
            }

            // 開始・終了頂点が存在するか確認
            if (!vertexToIndex.ContainsKey(startVertex) || !vertexToIndex.ContainsKey(endVertex))
            {
                Console.WriteLine($"ERROR: {startVertex} または {endVertex} がグラフに存在しません。");
                return new Tuple<List<string>, double>(null, double.PositiveInfinity);
            }

            int startIndex = vertexToIndex[startVertex];
            int endIndex = vertexToIndex[endVertex];

            // 距離行列 (dist) と経路復元用行列 (nextNode) を初期化
            double[,] dist = new double[numVertices, numVertices];
            int[,] nextNode = new int[numVertices, numVertices];

            // 無限大で初期化
            for (int i = 0; i < numVertices; i++)
            {
                for (int j = 0; j < numVertices; j++)
                {
                    dist[i, j] = double.PositiveInfinity;
                    nextNode[i, j] = -1; // -1はパスがないことを示す
                }
            }

            // 初期距離と経路復元情報を設定
            for (int i = 0; i < numVertices; i++)
            {
                dist[i, i] = 0; // 自分自身への距離は0
                string vertex = vertices[i];
                List<Tuple<string, double>> neighbors = GetNeighbors(vertex);
                if (neighbors != null)
                {
                    foreach (var neighborInfo in neighbors)
                    {
                        int j = vertexToIndex[neighborInfo.Item1];
                        dist[i, j] = neighborInfo.Item2;
                        nextNode[i, j] = j; // iからjへの直接辺の場合、iの次はj
                    }
                }
            }

            // ワーシャル-フロイド法の本体
            // k: 中継点として使用する頂点のインデックス
            for (int k = 0; k < numVertices; k++)
            {
                // i: 開始頂点のインデックス
                for (int i = 0; i < numVertices; i++)
                {
                    // j: 終了頂点のインデックス
                    for (int j = 0; j < numVertices; j++)
                    {
                        // i -> k -> j の経路が i -> j の現在の経路より短い場合
                        if (dist[i, k] != double.PositiveInfinity && dist[k, j] != double.PositiveInfinity && 
                            dist[i, k] + dist[k, j] < dist[i, j])
                        {
                            dist[i, j] = dist[i, k] + dist[k, j];
                            nextNode[i, j] = nextNode[i, k]; // iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点
                        }
                    }
                }
            }

            // 指定された開始・終了頂点間の最短経路と重みを取得
            double shortestDistance = dist[startIndex, endIndex];

            // 経路が存在しない場合 (距離がINF)
            if (double.IsPositiveInfinity(shortestDistance))
            {
                return new Tuple<List<string>, double>(null, double.PositiveInfinity);
            }

            // 経路を復元
            List<string> path = new List<string>();
            int u = startIndex;
            
            // 開始と終了が同じ場合は経路は開始頂点のみ
            if (u == endIndex)
            {
                path.Add(startVertex);
            }
            else
            {
                // nextNodeを使って経路をたどる
                while (u != -1 && u != endIndex)
                {
                    path.Add(indexToVertex[u]);
                    u = nextNode[u, endIndex];
                    
                    // 無限ループ防止のための簡易チェック (到達不能なのにnextNodeが-1でない場合など)
                    if (u != -1 && path.Count > 0 && indexToVertex[u] == path[path.Count - 1])
                    {
                        // 同じ頂点に戻ってきたなど、異常な経路復元を防ぐ
                        Console.WriteLine($"WARNING: 経路復元中に異常を検出しました（{indexToVertex[u]}でループ？）。");
                        path = null;
                        shortestDistance = double.PositiveInfinity;
                        break;
                    }
                }
                
                // 最後のノード (endVertex) を追加
                if (path != null)
                {
                    path.Add(endVertex);
                }
            }

            return new Tuple<List<string>, double>(path, shortestDistance);
        }
    }

    class Program
    {
        // ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
        static double DummyHeuristic(string u, string v)
        {
            // u と v の間に何らかの推定距離を計算する関数
            // ここではダミーとして常に0を返す
            return 0;
        }

        static void Main(string[] args)
        {
            Console.WriteLine("WarshallFloyd -----> start");

            GraphData graphData = new GraphData();

            graphData.Clear();
            var inputList1 = new List<Tuple<string, string, double>>
            {
                new Tuple<string, string, double>("A", "B", 4),
                new Tuple<string, string, double>("B", "C", 3),
                new Tuple<string, string, double>("B", "D", 2),
                new Tuple<string, string, double>("D", "A", 1),
                new Tuple<string, string, double>("A", "C", 2),
                new Tuple<string, string, double>("B", "D", 2)
            };
            foreach (var input in inputList1)
            {
                graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            }
            Console.WriteLine("\nグラフの頂点: " + string.Join(", ", graphData.GetVertices()));
            Console.WriteLine("グラフの辺 (重み付き): " + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
            var input1 = new Tuple<string, string>("A", "B");
            var shortestPath1 = graphData.GetShortestPath(input1.Item1, input1.Item2, DummyHeuristic);
            Console.WriteLine($"経路{input1.Item1}-{input1.Item2} の最短経路は {(shortestPath1.Item1 != null ? string.Join(", ", shortestPath1.Item1) : "None")} (重み: {shortestPath1.Item2})");

            graphData.Clear();
            var inputList2 = new List<Tuple<string, string, double>>
            {
                new Tuple<string, string, double>("A", "B", 4),
                new Tuple<string, string, double>("C", "D", 4),
                new Tuple<string, string, double>("E", "F", 1),
                new Tuple<string, string, double>("F", "G", 1)
            };
            foreach (var input in inputList2)
            {
                graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            }
            Console.WriteLine("\nグラフの頂点: " + string.Join(", ", graphData.GetVertices()));
            Console.WriteLine("グラフの辺 (重み付き): " + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
            var input2 = new Tuple<string, string>("A", "B");
            var shortestPath2 = graphData.GetShortestPath(input2.Item1, input2.Item2, DummyHeuristic);
            Console.WriteLine($"経路{input2.Item1}-{input2.Item2} の最短経路は {(shortestPath2.Item1 != null ? string.Join(", ", shortestPath2.Item1) : "None")} (重み: {shortestPath2.Item2})");

            graphData.Clear();
            var inputList3 = new List<Tuple<string, string, double>>
            {
                new Tuple<string, string, double>("A", "B", 4),
                new Tuple<string, string, double>("B", "C", 3),
                new Tuple<string, string, double>("D", "E", 5)
            };
            foreach (var input in inputList3)
            {
                graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            }
            Console.WriteLine("\nグラフの頂点: " + string.Join(", ", graphData.GetVertices()));
            Console.WriteLine("グラフの辺 (重み付き): " + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
            var input3 = new Tuple<string, string>("A", "D");
            var shortestPath3 = graphData.GetShortestPath(input3.Item1, input3.Item2, DummyHeuristic);
            Console.WriteLine($"経路{input3.Item1}-{input3.Item2} の最短経路は {(shortestPath3.Item1 != null ? string.Join(", ", shortestPath3.Item1) : "None")} (重み: {shortestPath3.Item2})");

            graphData.Clear();
            var inputList4 = new List<Tuple<string, string, double>>();
            foreach (var input in inputList4)
            {
                graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            }
            Console.WriteLine("\nグラフの頂点: " + string.Join(", ", graphData.GetVertices()));
            Console.WriteLine("グラフの辺 (重み付き): " + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
            var input4 = new Tuple<string, string>("A", "B");
            var shortestPath4 = graphData.GetShortestPath(input4.Item1, input4.Item2, DummyHeuristic);
            Console.WriteLine($"経路{input4.Item1}-{input4.Item2} の最短経路は {(shortestPath4.Item1 != null ? string.Join(", ", shortestPath4.Item1) : "None")} (重み: {shortestPath4.Item2})");

            Console.WriteLine("\nWarshallFloyd <----- end");
        }
    }
}