// C#
// グラフの最短経路: ベルマンフォード法 (Bellman Ford)

using System;
using System.Collections.Generic;
using System.Linq;

namespace BellmanFord
{
    class GraphData
    {
        // 隣接ノードとその辺の重みを格納します
        private Dictionary<string, List<(string neighbor, double weight)>> _data;

        public GraphData()
        {
            _data = new Dictionary<string, List<(string, double)>>();
        }

        public Dictionary<string, List<(string neighbor, double weight)>> Get()
        {
            // グラフの内部データを取得します
            return _data;
        }

        public List<string> GetVertices()
        {
            // グラフの全頂点をリストとして返します
            return _data.Keys.ToList();
        }

        public List<(string from, string to, double weight)> GetEdges()
        {
            // グラフの全辺をリストとして返します
            // ベルマン-フォード法で使用するため、内部データから有向辺として抽出します
            var edges = new List<(string from, string to, double weight)>();
            foreach (var u in _data.Keys)
            {
                foreach (var (v, weight) in _data[u])
                {
                    edges.Add((u, v, weight));
                }
            }
            return edges;
        }

        public bool AddVertex(string vertex)
        {
            // 新しい頂点をグラフに追加します
            if (!_data.ContainsKey(vertex))
            {
                _data[vertex] = new List<(string, double)>();
                return true;
            }
            return true; // 既に存在する場合は追加しないがTrueを返す
        }

        public bool AddEdge(string vertex1, string vertex2, double weight)
        {
            // 両頂点間に辺を追加します。重みを指定します
            // 頂点がグラフに存在しない場合は追加します
            AddVertex(vertex1);
            AddVertex(vertex2);

            // vertex1 -> vertex2 の辺を追加（重み付き）
            // 既に同じ頂点間の辺が存在する場合は重みを更新
            bool edgeUpdatedV1V2 = false;
            for (int i = 0; i < _data[vertex1].Count; i++)
            {
                if (_data[vertex1][i].neighbor == vertex2)
                {
                    _data[vertex1][i] = (vertex2, weight);
                    edgeUpdatedV1V2 = true;
                    break;
                }
            }
            if (!edgeUpdatedV1V2)
            {
                _data[vertex1].Add((vertex2, weight));
            }

            // vertex2 -> vertex1 の辺を追加（重み付き）
            // 既に同じ頂点間の辺が存在する場合は重みを更新
            bool edgeUpdatedV2V1 = false;
            for (int i = 0; i < _data[vertex2].Count; i++)
            {
                if (_data[vertex2][i].neighbor == vertex1)
                {
                    _data[vertex2][i] = (vertex1, weight);
                    edgeUpdatedV2V1 = true;
                    break;
                }
            }
            if (!edgeUpdatedV2V1)
            {
                _data[vertex2].Add((vertex1, weight));
            }

            return true;
        }

        public bool IsEmpty()
        {
            // グラフが空かどうかを返します
            return _data.Count == 0;
        }

        public bool Clear()
        {
            // グラフを空にします
            _data.Clear();
            return true;
        }

        public (List<string> path, double distance) GetShortestPath(string startVertex, string endVertex, Func<string, string, double> heuristic)
        {
            var vertices = GetVertices();
            var edges = GetEdges(); // 有向辺のリストを取得
            int numVertices = vertices.Count;

            // 始点と終点の存在チェック
            if (!vertices.Contains(startVertex))
            {
                Console.WriteLine($"エラー: 始点 '{startVertex}' がグラフに存在しません。");
                return (new List<string>(), double.PositiveInfinity);
            }
            if (!vertices.Contains(endVertex))
            {
                Console.WriteLine($"エラー: 終点 '{endVertex}' がグラフに存在しません。");
                return (new List<string>(), double.PositiveInfinity);
            }

            // 始点と終点が同じ場合
            if (startVertex == endVertex)
            {
                return (new List<string> { startVertex }, 0);
            }

            // 距離と先行頂点の初期化
            var dist = vertices.ToDictionary(v => v, v => double.PositiveInfinity);
            var pred = vertices.ToDictionary(v => v, v => (string)null);
            dist[startVertex] = 0; // 始点自身の距離は0

            // |V| - 1 回の緩和ステップを実行
            for (int i = 0; i < numVertices - 1; i++)
            {
                // 緩和が一度も行われなかった場合にループを中断するためのフラグ
                bool relaxedInThisIteration = false;
                foreach (var (u, v, weight) in edges)
                {
                    // dist[u] が無限大でない場合のみ緩和を試みる
                    if (dist[u] != double.PositiveInfinity && dist[u] + weight < dist[v])
                    {
                        dist[v] = dist[u] + weight;
                        pred[v] = u;
                        relaxedInThisIteration = true;
                    }
                }
                // このイテレーションで緩和が行われなかった場合は、ループを抜ける
                if (!relaxedInThisIteration)
                {
                    break;
                }
            }

            // 負閉路の検出
            foreach (var (u, v, weight) in edges)
            {
                if (dist[u] != double.PositiveInfinity && dist[u] + weight < dist[v])
                {
                    // 負閉路が存在します
                    Console.WriteLine("エラー: グラフに負閉路が存在します。最短経路は定義できません。");
                    return (null, double.NegativeInfinity); // 負の無限大を返す
                }
            }

            // 最短経路の構築
            var path = new List<string>();
            string current = endVertex;

            // 終点まで到達不可能かチェック (距離が初期値のままか)
            if (dist[endVertex] == double.PositiveInfinity)
            {
                return (new List<string>(), double.PositiveInfinity); // 到達不可能
            }

            // 終点から先行頂点をたどって経路を逆順に構築
            while (current != null)
            {
                path.Add(current);
                // 始点に到達したらループを終了
                if (current == startVertex)
                {
                    break;
                }
                // 次の頂点に進む
                current = pred[current];
            }

            // 経路が始点から始まっていない場合
            if (path.Count == 0 || path[path.Count - 1] != startVertex)
            {
                return (new List<string>(), double.PositiveInfinity);
            }

            path.Reverse(); // 経路を始点から終点の順にする

            return (path, dist[endVertex]);
        }
    }

    class Program
    {
        // ヒューリスティック関数 (ベルマン-フォード法では使用しないが、元のコードに合わせた引数のために残す)
        static double DummyHeuristic(string u, string v)
        {
            // ベルマン-フォード法では使用しないため、常に0を返す
            return 0;
        }

        static void Main(string[] args)
        {
            Console.WriteLine("BellmanFord TEST -----> start");

            var graphData = new GraphData();

            graphData.Clear();
            var inputList1 = new List<(string, string, double)>
            {
                ("A", "B", 4), ("B", "C", 3), ("B", "D", 2), ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)
            };
            foreach (var input in inputList1)
            {
                graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            }
            Console.WriteLine("\nグラフの頂点: " + string.Join(", ", graphData.GetVertices()));
            Console.WriteLine("グラフの辺 (重み付き): " + string.Join(", ", graphData.GetEdges().Select(e => $"({e.from}-{e.to}, {e.weight})")));
            var input1 = ("A", "B");
            var shortestPath1 = graphData.GetShortestPath(input1.Item1, input1.Item2, DummyHeuristic);
            Console.WriteLine($"経路{input1.Item1}-{input1.Item2} の最短経路は [{string.Join(", ", shortestPath1.path)}] (重み: {shortestPath1.distance})");

            graphData.Clear();
            var inputList2 = new List<(string, string, double)>
            {
                ("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)
            };
            foreach (var input in inputList2)
            {
                graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            }
            Console.WriteLine("\nグラフの頂点: " + string.Join(", ", graphData.GetVertices()));
            Console.WriteLine("グラフの辺 (重み付き): " + string.Join(", ", graphData.GetEdges().Select(e => $"({e.from}-{e.to}, {e.weight})")));
            var input2 = ("A", "B");
            var shortestPath2 = graphData.GetShortestPath(input2.Item1, input2.Item2, DummyHeuristic);
            Console.WriteLine($"経路{input2.Item1}-{input2.Item2} の最短経路は [{string.Join(", ", shortestPath2.path)}] (重み: {shortestPath2.distance})");

            graphData.Clear();
            var inputList3 = new List<(string, string, double)>
            {
                ("A", "B", 4), ("B", "C", 3), ("D", "E", 5)
            };
            foreach (var input in inputList3)
            {
                graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            }
            Console.WriteLine("\nグラフの頂点: " + string.Join(", ", graphData.GetVertices()));
            Console.WriteLine("グラフの辺 (重み付き): " + string.Join(", ", graphData.GetEdges().Select(e => $"({e.from}-{e.to}, {e.weight})")));
            var input3 = ("A", "D");
            var shortestPath3 = graphData.GetShortestPath(input3.Item1, input3.Item2, DummyHeuristic);
            Console.WriteLine($"経路{input3.Item1}-{input3.Item2} の最短経路は [{string.Join(", ", shortestPath3.path)}] (重み: {shortestPath3.distance})");

            graphData.Clear();
            var inputList4 = new List<(string, string, double)>();
            foreach (var input in inputList4)
            {
                graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            }
            Console.WriteLine("\nグラフの頂点: " + string.Join(", ", graphData.GetVertices()));
            Console.WriteLine("グラフの辺 (重み付き): " + string.Join(", ", graphData.GetEdges().Select(e => $"({e.from}-{e.to}, {e.weight})")));
            var input4 = ("A", "B");
            var shortestPath4 = graphData.GetShortestPath(input4.Item1, input4.Item2, DummyHeuristic);
            Console.WriteLine($"経路{input4.Item1}-{input4.Item2} の最短経路は [{string.Join(", ", shortestPath4.path)}] (重み: {shortestPath4.distance})");

            Console.WriteLine("\nBellmanFord TEST <----- end");
        }
    }
}