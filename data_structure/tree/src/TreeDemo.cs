// C#
// データ構造: 木 (Tree)

using System;
using System.Collections.Generic;
using System.Linq;

public class NodeData
{
    private object _value;
    private NodeData _parent;
    private List<NodeData> _children;

    public NodeData(object value)
    {
        _value = value;
        _parent = null;
        _children = new List<NodeData>();
    }

    public object GetValue()
    {
        return _value;
    }

    public NodeData GetParent()
    {
        return _parent;
    }

    public List<NodeData> GetChildren()
    {
        return _children;
    }

    public bool SetParent(NodeData parent)
    {
        _parent = parent;
        return true;
    }

    public bool AddChild(NodeData child)
    {
        child.SetParent(this);
        _children.Add(child);
        return true;
    }

    public bool RemoveChild(NodeData child)
    {
        if (_children.Contains(child))
        {
            child.SetParent(null);
            _children.Remove(child);
            return true;
        }
        else
        {
            return false;
        }
    }

    public bool IsLeaf()
    {
        return _children.Count == 0;
    }
}

public class TreeData
{
    private NodeData _data;

    public TreeData()
    {
        _data = null;
    }

    public NodeData Get()
    {
        return _data;
    }

    public int GetHeight(NodeData node = null)
    {
        if (node == null)
        {
            node = _data;
        }
        if (node == null)
        {
            return 0;
        }
        if (node.IsLeaf())
        {
            return 1;
        }
        return 1 + node.GetChildren().Select(child => GetHeight(child)).Max();
    }

    public NodeData GetParent(NodeData node)
    {
        return node.GetParent();
    }

    public List<NodeData> GetChildren(NodeData node)
    {
        return node.GetChildren();
    }

    public NodeData GetNode(object value, NodeData node = null)
    {
        if (node == null)
        {
            node = _data;
        }
        if (node == null)
        {
            return null;
        }

        if (node.GetValue().Equals(value))
        {
            return node;
        }

        foreach (var child in node.GetChildren())
        {
            var result = GetNode(value, child);
            if (result != null)
            {
                return result;
            }
        }

        return null;
    }

    public bool Add(NodeData parent, object value)
    {
        var newNode = new NodeData(value);
        if (parent == null)
        {
            if (_data == null)
            {
                _data = newNode;
                return true;
            }
            else
            {
                Console.WriteLine($"ERROR: {value} 重複です");
                return false;
            }
        }
        else
        {
            parent.AddChild(newNode);
            return true;
        }
    }

    public bool Remove(NodeData node)
    {
        if (node == null)
        {
            return false;
        }

        if (node == _data)
        {
            _data = null;
            return true;
        }

        var parent = node.GetParent();
        if (parent != null)
        {
            return parent.RemoveChild(node);
        }

        return false;
    }

    public List<object> Traverse(NodeData node = null, string mode = "pre-order")
    {
        if (node == null)
        {
            node = _data;
        }
        if (node == null)
        {
            return new List<object>();
        }

        var result = new List<object>();

        if (mode == "pre-order")
        {
            result.Add(node.GetValue());
            foreach (var child in node.GetChildren())
            {
                result.AddRange(Traverse(child, mode));
            }
        }
        else if (mode == "post-order")
        {
            foreach (var child in node.GetChildren())
            {
                result.AddRange(Traverse(child, mode));
            }
            result.Add(node.GetValue());
        }
        else if (mode == "level-order")
        {
            var queue = new Queue<NodeData>();
            queue.Enqueue(node);
            while (queue.Count > 0)
            {
                var current = queue.Dequeue();
                result.Add(current.GetValue());
                foreach (var child in current.GetChildren())
                {
                    queue.Enqueue(child);
                }
            }
        }

        return result;
    }

    public bool IsLeaf(NodeData node)
    {
        return node != null && node.IsLeaf();
    }

    public bool IsEmpty()
    {
        return _data == null;
    }

    public int Size(NodeData node = null)
    {
        if (node == null)
        {
            node = _data;
        }
        if (node == null)
        {
            return 0;
        }

        int count = 1;
        foreach (var child in node.GetChildren())
        {
            count += Size(child);
        }

        return count;
    }

    public bool Clear()
    {
        _data = null;
        return true;
    }

    public List<object> Display()
    {
        if (_data == null)
        {
            return new List<object>();
        }

        return Traverse(mode: "level-order");
    }
}

public class Program
{
    public static void Main()
    {
        Console.WriteLine("Tree TEST -----> start");

        Console.WriteLine("\nnew");
        var treeData = new TreeData();
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", treeData.Display())}]");

        Console.WriteLine("\nis_empty");
        var output = treeData.IsEmpty();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nsize");
        output = treeData.Size();
        Console.WriteLine($"  出力値: {output}");

        Console.WriteLine("\nadd");
        Console.WriteLine($"  入力値: (null, Root)");
        output = treeData.Add(null, "Root");
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", treeData.Display())}]");

        var rootNode = treeData.Get();

        Console.WriteLine("\nadd");
        Console.WriteLine($"  入力値: (rootNode, Child1)");
        output = treeData.Add(rootNode, "Child1");
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", treeData.Display())}]");

        Console.WriteLine("\nadd");
        Console.WriteLine($"  入力値: (rootNode, Child2)");
        output = treeData.Add(rootNode, "Child2");
        Console.WriteLine($"  出力値: {output}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", treeData.Display())}]");

        Console.WriteLine("\nget_node");
        var inputValue = "Child1";
        Console.WriteLine($"  入力値: {inputValue}");
        var nodeOutput = treeData.GetNode(inputValue);
        Console.WriteLine($"  出力値: {nodeOutput}");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", treeData.Display())}]");

        Console.WriteLine("\ntraverse");
        var inputMode = "pre-order";
        Console.WriteLine($"  入力値: {inputMode}");
        var traverseOutput = treeData.Traverse(mode: inputMode);
        Console.WriteLine($"  出力値: [{string.Join(", ", traverseOutput)}]");
        Console.WriteLine($"  現在のデータ: [{string.Join(", ", treeData.Display())}]");

        Console.WriteLine("\nTree TEST <----- end");
    }
}