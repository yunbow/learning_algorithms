// C#
// データ構造: グラフ (Graph)

using System;
using System.Collections.Generic;
using System.Linq;

// データ構造: グラフ (Graph)
class GraphData
{
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private Dictionary<string, List<(string Neighbor, int Weight)>> _data;

    public GraphData()
    {
        _data = new Dictionary<string, List<(string, int)>>();
    }

    public Dictionary<string, List<(string Neighbor, int Weight)>> Get()
    {
        // グラフの内部データを取得します。
        return _data;
    }

    public List<string> GetVertices()
    {
        // グラフの全頂点をリストとして返します。
        return _data.Keys.ToList();
    }

    public List<(string, string, int)> GetEdges()
    {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにハッシュセットを使用します。
        var edges = new HashSet<(string, string, int)>();
        foreach (var vertex in _data.Keys)
        {
            foreach (var (neighbor, weight) in _data[vertex])
            {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                var sortedVertices = new[] { vertex, neighbor }.OrderBy(v => v).ToArray();
                edges.Add((sortedVertices[0], sortedVertices[1], weight));
            }
        }
        return edges.ToList();
    }

    public List<(string, int)> GetNeighbors(string vertex)
    {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        if (_data.ContainsKey(vertex))
        {
            return _data[vertex];
        }
        return null; // 頂点が存在しない場合はnullを返す
    }

    public int? GetEdgeWeight(string vertex1, string vertex2)
    {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はnullを返します。
        if (_data.ContainsKey(vertex1) && _data.ContainsKey(vertex2))
        {
            foreach (var (neighbor, weight) in _data[vertex1])
            {
                if (neighbor == vertex2)
                {
                    return weight;
                }
            }
        }
        return null; // 辺が存在しない場合
    }

    public List<(string, int)> GetVertice(string vertex)
    {
        // 頂点がグラフに存在するか確認する
        if (_data.ContainsKey(vertex))
        {
            // 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            return _data[vertex];
        }
        else
        {
            // 存在しない場合はメッセージを表示し、nullを返す
            Console.WriteLine($"ERROR: {vertex}は範囲外です");
            return null;
        }
    }

    public bool GetEdge(string vertex1, string vertex2)
    {
        // 指定された2つの頂点間に辺が存在するかを確認する
        // 両方の頂点がグラフに存在する必要がある
        if (_data.ContainsKey(vertex1) && _data.ContainsKey(vertex2))
        {
            // vertex1の隣接リストにvertex2が含まれているかを確認
            return _data[vertex1].Any(edge => edge.Neighbor == vertex2);
        }
        else
        {
            // どちらかの頂点が存在しない場合は辺も存在しない
            return false;
        }
    }

    public bool AddVertex(string vertex)
    {
        // 新しい頂点をグラフに追加します。
        if (!_data.ContainsKey(vertex))
        {
            _data[vertex] = new List<(string, int)>();
        }
        // 既に存在する場合も成功とみなす
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

        // vertex1 -> vertex2 の辺を追加（重み付き）
        bool edgeExistsV1V2 = false;
        for (int i = 0; i < _data[vertex1].Count; i++)
        {
            if (_data[vertex1][i].Neighbor == vertex2)
            {
                _data[vertex1][i] = (vertex2, weight); // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true;
                break;
            }
        }
        if (!edgeExistsV1V2)
        {
            _data[vertex1].Add((vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edgeExistsV2V1 = false;
        for (int i = 0; i < _data[vertex2].Count; i++)
        {
            if (_data[vertex2][i].Neighbor == vertex1)
            {
                _data[vertex2][i] = (vertex1, weight); // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true;
                break;
            }
        }
        if (!edgeExistsV2V1)
        {
            _data[vertex2].Add((vertex1, weight));
        }

        return true;
    }

    public bool RemoveVertex(string vertex)
    {
        // 頂点とそれに関連する辺を削除する
        if (_data.ContainsKey(vertex))
        {
            // この頂点への参照を他の頂点の隣接リストから削除する
            foreach (var v in _data.Keys.ToList())
            {
                _data[v] = _data[v].Where(edge => edge.Neighbor != vertex).ToList();
            }
            // 頂点自体を削除する
            _data.Remove(vertex);
            return true;
        }
        else
        {
            Console.WriteLine($"ERROR: {vertex} は範囲外です");
            return false;
        }
    }

    public bool RemoveEdge(string vertex1, string vertex2)
    {
        // 両頂点間の辺を削除します。
        if (_data.ContainsKey(vertex1) && _data.ContainsKey(vertex2))
        {
            bool removed = false;
            
            // vertex1 から vertex2 への辺を削除
            int originalLenV1 = _data[vertex1].Count;
            _data[vertex1] = _data[vertex1].Where(edge => edge.Neighbor != vertex2).ToList();
            if (_data[vertex1].Count < originalLenV1)
            {
                removed = true;
            }

            // vertex2 から vertex1 への辺を削除
            int originalLenV2 = _data[vertex2].Count;
            _data[vertex2] = _data[vertex2].Where(edge => edge.Neighbor != vertex1).ToList();
            if (_data[vertex2].Count < originalLenV2)
            {
                removed = true;
            }

            return removed; // 少なくとも片方向が削除されたか
        }
        else
        {
            Console.WriteLine($"ERROR: {vertex1} または {vertex2} は範囲外です");
            return false;
        }
    }

    public bool IsEmpty()
    {
        // グラフが空かどうか
        return _data.Count == 0;
    }

    public int Size()
    {
        // グラフの頂点数を返す
        return _data.Count;
    }

    public bool Clear()
    {
        // グラフを空にする
        _data.Clear();
        return true;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Graph TEST -----> start");

        Console.WriteLine("\nnew");
        var graphData = new GraphData();
        Console.WriteLine($"  現在のデータ: {DictionaryToString(graphData.Get())}");

        Console.WriteLine("\nis_empty");
        var output = graphData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsize");
        output = graphData.Size();
        Console.WriteLine($"  出力値: {output}");

        var inputList = new[] { "A", "B", "C" };
        foreach (var input in inputList)
        {
            Console.WriteLine("\nadd_vertex");
            Console.WriteLine($"  入力値: {input}");
            var boolOutput = graphData.AddVertex(input);
            Console.WriteLine($"  出力値: {boolOutput}");
        }
        Console.WriteLine($"  現在のデータ: {DictionaryToString(graphData.Get())}");

        Console.WriteLine("\nget_vertices");
        var verticesOutput = graphData.GetVertices();
        Console.WriteLine($"  出力値: {ListToString(verticesOutput)}");

        Console.WriteLine("\nsize");
        output = graphData.Size();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nadd_edge");
        var edgeInputList = new[] { ("A", "B", 4), ("B", "C", 2), ("C", "A", 3) };
        foreach (var input in edgeInputList)
        {
            Console.WriteLine($"  入力値: {input}");
            var boolOutput = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {boolOutput}");
        }
        Console.WriteLine($"  現在のデータ: {DictionaryToString(graphData.Get())}");

        Console.WriteLine("\nget_vertices");
        verticesOutput = graphData.GetVertices();
        Console.WriteLine($"  出力値: {ListToString(verticesOutput)}");

        Console.WriteLine("\nget_edges");
        var edgesOutput = graphData.GetEdges();
        Console.WriteLine($"  出力値: {EdgesToString(edgesOutput)}");

        Console.WriteLine("\nsize");
        output = graphData.Size();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nget_vertice");
        var vertexInput = "B";
        Console.WriteLine($"  入力値: '{vertexInput}'");
        var verticeOutput = graphData.GetVertice(vertexInput);
        Console.WriteLine($"  出力値: {VerticeToString(verticeOutput)}");

        Console.WriteLine("\nget_vertice");
        vertexInput = "E";
        Console.WriteLine($"  入力値: '{vertexInput}'");
        verticeOutput = graphData.GetVertice(vertexInput);
        Console.WriteLine($"  出力値: {verticeOutput}");

        Console.WriteLine("\nremove_edge");
        var edgeInput = ("A", "B");
        Console.WriteLine($"  入力値: {edgeInput}");
        var boolOutput = graphData.RemoveEdge(edgeInput.Item1, edgeInput.Item2);
        Console.WriteLine($"  出力値: {boolOutput}");
        Console.WriteLine($"  現在のデータ: {DictionaryToString(graphData.Get())}");

        Console.WriteLine("\nremove_edge");
        edgeInput = ("A", "C");
        Console.WriteLine($"  入力値: {edgeInput}");
        boolOutput = graphData.RemoveEdge(edgeInput.Item1, edgeInput.Item2);
        Console.WriteLine($"  出力値: {boolOutput}");
        Console.WriteLine($"  現在のデータ: {DictionaryToString(graphData.Get())}");

        Console.WriteLine("\nget_edges");
        edgesOutput = graphData.GetEdges();
        Console.WriteLine($"  出力値: {EdgesToString(edgesOutput)}");

        Console.WriteLine("\nremove_vertex");
        vertexInput = "B";
        Console.WriteLine($"  入力値: {vertexInput}");
        boolOutput = graphData.RemoveVertex(vertexInput);
        Console.WriteLine($"  出力値: {boolOutput}");
        Console.WriteLine($"  現在のデータ: {DictionaryToString(graphData.Get())}");

        Console.WriteLine("\nremove_vertex");
        vertexInput = "Z";
        Console.WriteLine($"  入力値: {vertexInput}");
        boolOutput = graphData.RemoveVertex(vertexInput);
        Console.WriteLine($"  出力値: {boolOutput}");
        Console.WriteLine($"  現在のデータ: {DictionaryToString(graphData.Get())}");

        Console.WriteLine("\nget_vertices");
        verticesOutput = graphData.GetVertices();
        Console.WriteLine($"  出力値: {ListToString(verticesOutput)}");

        Console.WriteLine("\nget_edges");
        edgesOutput = graphData.GetEdges();
        Console.WriteLine($"  出力値: {EdgesToString(edgesOutput)}");

        Console.WriteLine("\nsize");
        output = graphData.Size();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nget_vertice");
        vertexInput = "B";
        Console.WriteLine($"  入力値: {vertexInput}");
        verticeOutput = graphData.GetVertice(vertexInput);
        Console.WriteLine($"  出力値: {verticeOutput}");

        Console.WriteLine("\nclear");
        boolOutput = graphData.Clear();
        Console.WriteLine($"  出力値: {boolOutput}");

        Console.WriteLine("\nis_empty");
        boolOutput = graphData.IsEmpty();
        Console.WriteLine($"  出力値: {boolOutput}");

        Console.WriteLine("\nsize");
        output = graphData.Size();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nget_vertices");
        verticesOutput = graphData.GetVertices();
        Console.WriteLine($"  出力値: {ListToString(verticesOutput)}");

        Console.WriteLine("\nget_edges");
        edgesOutput = graphData.GetEdges();
        Console.WriteLine($"  出力値: {EdgesToString(edgesOutput)}");

        Console.WriteLine("\nGraph TEST <----- end");
    }

    // ヘルパーメソッド：グラフデータの文字列表現
    static string DictionaryToString<T>(Dictionary<string, List<(string, int)>> dict)
    {
        if (dict.Count == 0)
            return "{}";

        var result = "{";
        foreach (var kvp in dict)
        {
            result += $"'{kvp.Key}': {VerticeToString(kvp.Value)}, ";
        }
        if (dict.Count > 0)
            result = result.Substring(0, result.Length - 2);
        result += "}";
        return result;
    }

    // ヘルパーメソッド：頂点リストの文字列表現
    static string ListToString<T>(List<T> list)
    {
        if (list == null || list.Count == 0)
            return "[]";

        var result = "[";
        foreach (var item in list)
        {
            result += $"'{item}', ";
        }
        result = result.Substring(0, result.Length - 2);
        result += "]";
        return result;
    }

    // ヘルパーメソッド：辺リストの文字列表現
    static string EdgesToString(List<(string, string, int)> edges)
    {
        if (edges == null || edges.Count == 0)
            return "[]";

        var result = "[";
        foreach (var (v1, v2, weight) in edges)
        {
            result += $"('{v1}', '{v2}', {weight}), ";
        }
        if (edges.Count > 0)
            result = result.Substring(0, result.Length - 2);
        result += "]";
        return result;
    }

    // ヘルパーメソッド：隣接リストの文字列表現
    static string VerticeToString(List<(string Neighbor, int Weight)> vertice)
    {
        if (vertice == null || vertice.Count == 0)
            return "[]";

        var result = "[";
        foreach (var (neighbor, weight) in vertice)
        {
            result += $"('{neighbor}', {weight}), ";
        }
        if (vertice.Count > 0)
            result = result.Substring(0, result.Length - 2);
        result += "]";
        return result;
    }
}