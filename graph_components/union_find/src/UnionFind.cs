// C#
// グラフの連結成分: Union-Find

using System;
using System.Collections.Generic;
using System.Linq;

public class GraphData
{
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private Dictionary<string, List<Tuple<string, int>>> _data;

    public GraphData()
    {
        _data = new Dictionary<string, List<Tuple<string, int>>>();
    }

    public Dictionary<string, List<Tuple<string, int>>> Get()
    {
        // グラフの内部データを取得します。
        return _data;
    }

    public List<string> GetVertices()
    {
        // グラフの全頂点をリストとして返します。
        return _data.Keys.ToList();
    }

    public List<Tuple<string, string, int>> GetEdges()
    {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        HashSet<Tuple<string, string, int>> edges = new HashSet<Tuple<string, string, int>>();
        
        foreach (var vertex in _data.Keys)
        {
            foreach (var neighborInfo in _data[vertex])
            {
                string neighbor = neighborInfo.Item1;
                int weight = neighborInfo.Item2;
                
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                string[] sortedVertices = new[] { vertex, neighbor }.OrderBy(v => v).ToArray();
                edges.Add(new Tuple<string, string, int>(sortedVertices[0], sortedVertices[1], weight));
            }
        }
        
        return edges.ToList();
    }

    public bool AddVertex(string vertex)
    {
        // 新しい頂点をグラフに追加します。
        if (!_data.ContainsKey(vertex))
        {
            _data[vertex] = new List<Tuple<string, int>>();
            return true;
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true;
    }

    public bool AddEdge(string vertex1, string vertex2, int weight)
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
        // 既に同じ辺が存在する場合は重みを更新する

        // vertex1 -> vertex2 の辺を追加（重み付き）
        bool edgeExistsV1V2 = false;
        for (int i = 0; i < _data[vertex1].Count; i++)
        {
            if (_data[vertex1][i].Item1 == vertex2)
            {
                _data[vertex1][i] = new Tuple<string, int>(vertex2, weight); // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true;
                break;
            }
        }
        if (!edgeExistsV1V2)
        {
            _data[vertex1].Add(new Tuple<string, int>(vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edgeExistsV2V1 = false;
        for (int i = 0; i < _data[vertex2].Count; i++)
        {
            if (_data[vertex2][i].Item1 == vertex1)
            {
                _data[vertex2][i] = new Tuple<string, int>(vertex1, weight); // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true;
                break;
            }
        }
        if (!edgeExistsV2V1)
        {
            _data[vertex2].Add(new Tuple<string, int>(vertex1, weight));
        }

        return true;
    }

    public bool IsEmpty()
    {
        // グラフが空かどうか
        return _data.Count == 0;
    }

    public bool Clear()
    {
        // グラフを空にする
        _data.Clear();
        return true;
    }

    public List<List<string>> GetConnectedComponents()
    {
        if (!_data.Any())
        {
            return new List<List<string>>(); // 空のグラフの場合は空リストを返す
        }

        // Union-Findのためのデータ構造を初期化
        Dictionary<string, string> parent = new Dictionary<string, string>();
        Dictionary<string, int> size = new Dictionary<string, int>();

        // 各頂点を初期状態では自分自身の集合に属させ、サイズを1とする
        List<string> vertices = GetVertices();
        foreach (string vertex in vertices)
        {
            parent[vertex] = vertex;
            size[vertex] = 1;
        }

        // 経路圧縮 (Path Compression) を伴う Find 操作
        string Find(string v)
        {
            // vの親がv自身でなければ、根を探しにいく
            if (parent[v] != v)
            {
                // 見つけた根をvの直接の親として記録 (経路圧縮)
                parent[v] = Find(parent[v]);
            }
            return parent[v]; // 最終的に根を返す
        }

        // Union by Size を伴う Union 操作
        bool Union(string u, string v)
        {
            string rootU = Find(u);
            string rootV = Find(v);

            // 根が同じ場合は、すでに同じ集合に属しているので何もしない
            if (rootU != rootV)
            {
                // より小さいサイズの木を大きいサイズの木に結合する
                if (size[rootU] < size[rootV])
                {
                    parent[rootU] = rootV;
                    size[rootV] += size[rootU];
                }
                else
                {
                    parent[rootV] = rootU;
                    size[rootU] += size[rootV];
                }
                return true; // 結合が行われた
            }
            return false; // 結合は行われなかった
        }

        // グラフの全ての辺に対してUnion操作を行い、連結成分をマージする
        foreach (var edge in GetEdges())
        {
            string u = edge.Item1;
            string v = edge.Item2;
            Union(u, v);
        }

        // 連結成分をグループ化する
        // 根をキーとして、その根に属する頂点のリストを値とする辞書を作成
        Dictionary<string, List<string>> components = new Dictionary<string, List<string>>();
        foreach (string vertex in vertices)
        {
            string root = Find(vertex); // 各頂点の最終的な根を見つける
            if (!components.ContainsKey(root))
            {
                components[root] = new List<string>();
            }
            components[root].Add(vertex);
        }

        // 連結成分のリスト（値の部分）を返す
        return components.Values.ToList();
    }
}

public class Program
{
    public static void Main()
    {
        Console.WriteLine("UnionFind TEST -----> start");

        Console.WriteLine("\nnew");
        GraphData graphData = new GraphData();
        Console.WriteLine($"  現在のデータ: {PrintDictionary(graphData.Get())}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList1 = new List<Tuple<string, string, int>>
        {
            new Tuple<string, string, int>("A", "B", 4),
            new Tuple<string, string, int>("B", "C", 3),
            new Tuple<string, string, int>("B", "D", 2),
            new Tuple<string, string, int>("D", "A", 1),
            new Tuple<string, string, int>("A", "C", 2),
            new Tuple<string, string, int>("B", "D", 2)
        };

        foreach (var input in inputList1)
        {
            Console.WriteLine($"  入力値: {input}");
            bool output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {PrintDictionary(graphData.Get())}");
        Console.WriteLine("\nget_connected_components");
        var components1 = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {PrintComponents(components1)}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList2 = new List<Tuple<string, string, int>>
        {
            new Tuple<string, string, int>("A", "B", 4),
            new Tuple<string, string, int>("C", "D", 4),
            new Tuple<string, string, int>("E", "F", 1),
            new Tuple<string, string, int>("F", "G", 1)
        };

        foreach (var input in inputList2)
        {
            Console.WriteLine($"  入力値: {input}");
            bool output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {PrintDictionary(graphData.Get())}");
        Console.WriteLine("\nget_connected_components");
        var components2 = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {PrintComponents(components2)}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList3 = new List<Tuple<string, string, int>>
        {
            new Tuple<string, string, int>("A", "B", 4),
            new Tuple<string, string, int>("B", "C", 3),
            new Tuple<string, string, int>("D", "E", 5)
        };

        foreach (var input in inputList3)
        {
            Console.WriteLine($"  入力値: {input}");
            bool output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {PrintDictionary(graphData.Get())}");
        Console.WriteLine("\nget_connected_components");
        var components3 = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {PrintComponents(components3)}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList4 = new List<Tuple<string, string, int>>(); // 空リスト
        foreach (var input in inputList4)
        {
            Console.WriteLine($"  入力値: {input}");
            bool output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {PrintDictionary(graphData.Get())}");
        Console.WriteLine("\nget_connected_components");
        var components4 = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {PrintComponents(components4)}");

        Console.WriteLine("\nUnionFind TEST <----- end");
    }

    // 辞書を文字列として表示するヘルパーメソッド
    private static string PrintDictionary(Dictionary<string, List<Tuple<string, int>>> dict)
    {
        List<string> entries = new List<string>();
        foreach (var key in dict.Keys)
        {
            List<string> neighbors = new List<string>();
            foreach (var neighborInfo in dict[key])
            {
                neighbors.Add($"('{neighborInfo.Item1}', {neighborInfo.Item2})");
            }
            entries.Add($"'{key}': [{string.Join(", ", neighbors)}]");
        }
        return "{" + string.Join(", ", entries) + "}";
    }

    // 連結成分を文字列として表示するヘルパーメソッド
    private static string PrintComponents(List<List<string>> components)
    {
        List<string> componentStrings = new List<string>();
        foreach (var component in components)
        {
            List<string> vertices = new List<string>();
            foreach (var vertex in component)
            {
                vertices.Add($"'{vertex}'");
            }
            componentStrings.Add($"[{string.Join(", ", vertices)}]");
        }
        return "[" + string.Join(", ", componentStrings) + "]";
    }
}