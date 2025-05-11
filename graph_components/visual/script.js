document.addEventListener('DOMContentLoaded', function() {
    const graphContainer = document.getElementById('graph-container');
    const algorithmSelect = document.getElementById('algorithm');
    const startBtn = document.getElementById('start-btn');
    const clearBtn = document.getElementById('clear-btn');
    const stepCount = document.getElementById('step-count');
    const stepDescription = document.getElementById('step-description');
    const componentCount = document.getElementById('component-count');
    const componentList = document.getElementById('component-list');

    // グラフデータ
    let nodes = [];
    let edges = [];
    let adjacencyList = [];
    let nodeElements = [];
    let edgeElements = [];
    
    // アルゴリズムの実行状態
    let isRunning = false;
    let visitedNodes = new Set();
    let components = [];
    let currentStep = 0;
    
    // アニメーションの速度（ミリ秒）
    const ANIMATION_SPEED = 1000;
    
    // 初期グラフの作成
    function initializeGraph() {
        clearGraph();
        
        // ノードの作成（頂点の数をランダムに決定: 8-12個）
        const nodeCount = Math.floor(Math.random() * 5) + 8;
        
        // ノードの配置
        for (let i = 0; i < nodeCount; i++) {
            const node = {
                id: i,
                x: Math.random() * (graphContainer.offsetWidth - 80) + 40,
                y: Math.random() * (graphContainer.offsetHeight - 80) + 40
            };
            nodes.push(node);
            
            // ノード要素の作成
            const nodeElement = document.createElement('div');
            nodeElement.className = 'node';
            nodeElement.textContent = i;
            nodeElement.style.left = `${node.x - 20}px`;
            nodeElement.style.top = `${node.y - 20}px`;
            graphContainer.appendChild(nodeElement);
            nodeElements.push(nodeElement);
        }
        
        // 隣接リストの初期化
        adjacencyList = Array(nodeCount).fill().map(() => []);
        
        // ランダムなエッジの作成（連結成分が複数になるように調整）
        const componentCount = Math.floor(Math.random() * 3) + 2; // 2-4個の連結成分
        const nodesPerComponent = Math.floor(nodeCount / componentCount);
        
        // 各連結成分内でのエッジを作成
        for (let c = 0; c < componentCount; c++) {
            const start = c * nodesPerComponent;
            const end = (c === componentCount - 1) ? nodeCount : (c + 1) * nodesPerComponent;
            
            // 各連結成分内のノードを接続
            for (let i = start; i < end; i++) {
                for (let j = i + 1; j < end; j++) {
                    // 50%の確率でエッジを追加
                    if (Math.random() < 0.5) {
                        const edge = { source: i, target: j };
                        edges.push(edge);
                        adjacencyList[i].push(j);
                        adjacencyList[j].push(i);
                        
                        // エッジ要素の作成
                        createEdgeElement(i, j);
                    }
                }
            }
        }
        
        // 稀に連結成分をつなげる（10%の確率）
        if (Math.random() < 0.1) {
            const component1 = Math.floor(Math.random() * componentCount);
            let component2;
            do {
                component2 = Math.floor(Math.random() * componentCount);
            } while (component1 === component2);
            
            const node1 = component1 * nodesPerComponent + Math.floor(Math.random() * nodesPerComponent);
            const node2 = component2 * nodesPerComponent + Math.floor(Math.random() * nodesPerComponent);
            
            const edge = { source: node1, target: node2 };
            edges.push(edge);
            adjacencyList[node1].push(node2);
            adjacencyList[node2].push(node1);
            
            createEdgeElement(node1, node2);
        }
        
        updateStepInfo(0, "グラフが初期化されました。アルゴリズムを選択して実行ボタンを押してください。");
        updateComponentInfo(0, []);
    }
    
    // エッジ要素の作成
    function createEdgeElement(source, target) {
        const sourceNode = nodes[source];
        const targetNode = nodes[target];
        
        const dx = targetNode.x - sourceNode.x;
        const dy = targetNode.y - sourceNode.y;
        const length = Math.sqrt(dx * dx + dy * dy);
        const angle = Math.atan2(dy, dx) * 180 / Math.PI;
        
        const edge = document.createElement('div');
        edge.className = 'edge';
        edge.style.width = `${length}px`;
        edge.style.left = `${sourceNode.x}px`;
        edge.style.top = `${sourceNode.y}px`;
        edge.style.transform = `rotate(${angle}deg)`;
        edge.dataset.source = source;
        edge.dataset.target = target;
        
        graphContainer.appendChild(edge);
        edgeElements.push(edge);
    }
    
    // グラフのクリア
    function clearGraph() {
        nodes = [];
        edges = [];
        adjacencyList = [];
        visitedNodes.clear();
        components = [];
        currentStep = 0;
        
        while (graphContainer.firstChild) {
            graphContainer.removeChild(graphContainer.firstChild);
        }
        
        nodeElements = [];
        edgeElements = [];
        
        updateStepInfo(0, "グラフをクリアしました。");
        updateComponentInfo(0, []);
    }
    
    // ステップ情報の更新
    function updateStepInfo(step, description) {
        currentStep = step;
        stepCount.textContent = step;
        stepDescription.textContent = description;
    }
    
    // 連結成分情報の更新
    function updateComponentInfo(count, componentsList) {
        componentCount.textContent = count;
        componentList.innerHTML = '';
        
        componentsList.forEach((component, index) => {
            const componentElement = document.createElement('div');
            componentElement.className = `component component-${index % 8}`;
            componentElement.textContent = component.join(', ');
            componentList.appendChild(componentElement);
        });
    }
    
    // DFSアルゴリズム
    async function runDFS() {
        updateStepInfo(1, "深さ優先探索（DFS）を開始します。");
        
        visitedNodes.clear();
        components = [];
        let componentIndex = 0;
        
        for (let i = 0; i < nodes.length; i++) {
            if (!visitedNodes.has(i)) {
                updateStepInfo(currentStep + 1, `新しい連結成分の探索を開始: ノード ${i} から`);
                
                // 新しい連結成分
                components[componentIndex] = [];
                await dfs(i, componentIndex);
                componentIndex++;
                
                updateComponentInfo(components.length, components);
            }
        }
        
        updateStepInfo(currentStep + 1, `DFSが完了しました。${components.length}つの連結成分が見つかりました。`);
        colorComponentNodes();
    }
    
    // DFSの再帰関数
    async function dfs(nodeIndex, componentIndex) {
        // 現在のノードを訪問中としてマーク
        nodeElements[nodeIndex].classList.add('current');
        await sleep(ANIMATION_SPEED / 2);
        
        visitedNodes.add(nodeIndex);
        components[componentIndex].push(nodeIndex);
        
        updateStepInfo(currentStep + 1, `ノード ${nodeIndex} を訪問します。現在の連結成分: [${components[componentIndex].join(', ')}]`);
        
        // 訪問済みとしてマーク
        nodeElements[nodeIndex].classList.remove('current');
        nodeElements[nodeIndex].classList.add('visited');
        await sleep(ANIMATION_SPEED / 2);
        
        // 隣接ノードの探索
        for (const neighbor of adjacencyList[nodeIndex]) {
            // エッジをアクティブにする
            const edgeElement = findEdgeElement(nodeIndex, neighbor);
            if (edgeElement) {
                edgeElement.classList.add('active');
                await sleep(ANIMATION_SPEED / 3);
            }
            
            if (!visitedNodes.has(neighbor)) {
                updateStepInfo(currentStep + 1, `ノード ${nodeIndex} から隣接ノード ${neighbor} を探索します。`);
                
                // エッジを訪問済みとしてマーク
                if (edgeElement) {
                    edgeElement.classList.remove('active');
                    edgeElement.classList.add('visited');
                }
                
                await dfs(neighbor, componentIndex);
            } else {
                updateStepInfo(currentStep + 1, `ノード ${neighbor} は既に訪問済みです。スキップします。`);
                
                // アクティブなエッジを元に戻す
                if (edgeElement) {
                    edgeElement.classList.remove('active');
                }
                
                await sleep(ANIMATION_SPEED / 3);
            }
        }
    }
    
    // BFSアルゴリズム
    async function runBFS() {
        updateStepInfo(1, "幅優先探索（BFS）を開始します。");
        
        visitedNodes.clear();
        components = [];
        let componentIndex = 0;
        
        for (let i = 0; i < nodes.length; i++) {
            if (!visitedNodes.has(i)) {
                updateStepInfo(currentStep + 1, `新しい連結成分の探索を開始: ノード ${i} から`);
                
                // 新しい連結成分
                components[componentIndex] = [];
                await bfs(i, componentIndex);
                componentIndex++;
                
                updateComponentInfo(components.length, components);
            }
        }
        
        updateStepInfo(currentStep + 1, `BFSが完了しました。${components.length}つの連結成分が見つかりました。`);
        colorComponentNodes();
    }
    
    // BFSの実装
    async function bfs(startNode, componentIndex) {
        const queue = [startNode];
        visitedNodes.add(startNode);
        components[componentIndex].push(startNode);
        
        updateStepInfo(currentStep + 1, `ノード ${startNode} をキューに追加します。`);
        
        nodeElements[startNode].classList.add('current');
        await sleep(ANIMATION_SPEED / 2);
        
        nodeElements[startNode].classList.remove('current');
        nodeElements[startNode].classList.add('visited');
        
        while (queue.length > 0) {
            const currentNode = queue.shift();
            
            updateStepInfo(currentStep + 1, `キューからノード ${currentNode} を取り出します。現在の連結成分: [${components[componentIndex].join(', ')}]`);
            
            // 隣接ノードの探索
            for (const neighbor of adjacencyList[currentNode]) {
                // エッジをアクティブにする
                const edgeElement = findEdgeElement(currentNode, neighbor);
                if (edgeElement) {
                    edgeElement.classList.add('active');
                    await sleep(ANIMATION_SPEED / 3);
                }
                
                if (!visitedNodes.has(neighbor)) {
                    updateStepInfo(currentStep + 1, `ノード ${currentNode} から隣接ノード ${neighbor} を探索します。`);
                    
                    visitedNodes.add(neighbor);
                    components[componentIndex].push(neighbor);
                    queue.push(neighbor);
                    
                    nodeElements[neighbor].classList.add('current');
                    await sleep(ANIMATION_SPEED / 2);
                    
                    nodeElements[neighbor].classList.remove('current');
                    nodeElements[neighbor].classList.add('visited');
                    
                    // エッジを訪問済みとしてマーク
                    if (edgeElement) {
                        edgeElement.classList.remove('active');
                        edgeElement.classList.add('visited');
                    }
                } else {
                    updateStepInfo(currentStep + 1, `ノード ${neighbor} は既に訪問済みです。スキップします。`);
                    
                    // アクティブなエッジを元に戻す
                    if (edgeElement) {
                        edgeElement.classList.remove('active');
                    }
                    
                    await sleep(ANIMATION_SPEED / 3);
                }
            }
        }
    }
    
    // Union-Find アルゴリズム
    async function runUnionFind() {
        updateStepInfo(1, "Union-Find アルゴリズムを開始します。");
        
        // 初期化
        let parent = Array(nodes.length).fill().map((_, i) => i);
        let rank = Array(nodes.length).fill(0);
        let componentMap = {};
        
        // Find操作（経路圧縮あり）
        function find(x) {
            if (parent[x] !== x) {
                parent[x] = find(parent[x]); // 経路圧縮
            }
            return parent[x];
        }
        
        // 各ノードを独立した連結成分として初期化
        for (let i = 0; i < nodes.length; i++) {
            nodeElements[i].classList.add('active');
            await sleep(ANIMATION_SPEED / 3);
            
            updateStepInfo(currentStep + 1, `ノード ${i} を独立した連結成分として初期化します。`);
            nodeElements[i].classList.remove('active');
        }
        
        // 各エッジについて、連結成分を結合
        for (const edge of edges) {
            const source = edge.source;
            const target = edge.target;
            
            // エッジをハイライト
            const edgeElement = findEdgeElement(source, target);
            if (edgeElement) {
                edgeElement.classList.add('active');
            }
            
            nodeElements[source].classList.add('current');
            nodeElements[target].classList.add('current');
            
            updateStepInfo(currentStep + 1, `エッジ ${source}-${target} を処理します。`);
            await sleep(ANIMATION_SPEED / 2);
            
            const rootX = find(source);
            const rootY = find(target);
            
            updateStepInfo(currentStep + 1, `ノード ${source} の根は ${rootX}、ノード ${target} の根は ${rootY} です。`);
            await sleep(ANIMATION_SPEED / 2);
            
            if (rootX !== rootY) {
                // Unionバイランク
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    updateStepInfo(currentStep + 1, `ノード ${rootX} の連結成分をノード ${rootY} の連結成分に統合します。`);
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                    updateStepInfo(currentStep + 1, `ノード ${rootY} の連結成分をノード ${rootX} の連結成分に統合します。`);
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                    updateStepInfo(currentStep + 1, `ノード ${rootY} の連結成分をノード ${rootX} の連結成分に統合し、ランクを増やします。`);
                }
            } else {
                updateStepInfo(currentStep + 1, `ノード ${source} とノード ${target} は既に同じ連結成分に属しています。`);
            }
            
            if (edgeElement) {
                edgeElement.classList.remove('active');
                edgeElement.classList.add('visited');
            }
            
            nodeElements[source].classList.remove('current');
            nodeElements[target].classList.remove('current');
            
            await sleep(ANIMATION_SPEED / 2);
        }
        
        // 連結成分の集計
        for (let i = 0; i < nodes.length; i++) {
            const root = find(i);
            if (!componentMap[root]) {
                componentMap[root] = [];
            }
            componentMap[root].push(i);
            
            nodeElements[i].classList.add('visited');
        }
        
        // 連結成分のリストを作成
        components = Object.values(componentMap);
        
        updateStepInfo(currentStep + 1, `Union-Findが完了しました。${components.length}つの連結成分が見つかりました。`);
        updateComponentInfo(components.length, components);
        
        // 連結成分ごとに色分け
        colorComponentNodes();
    }
    
    // 連結成分ごとに色分け
    function colorComponentNodes() {
        // 一旦すべてのノードとエッジのクラスをリセット
        nodeElements.forEach(node => {
            node.className = 'node';
        });
        
        edgeElements.forEach(edge => {
            edge.className = 'edge';
        });
        
        // 各連結成分ごとに色分け
        components.forEach((component, index) => {
            const colorClass = `component-${index % 8}`;
            
            component.forEach(nodeIndex => {
                nodeElements[nodeIndex].classList.add(colorClass);
                
                // そのノードに接続するエッジも同じ色に
                adjacencyList[nodeIndex].forEach(neighbor => {
                    if (component.includes(neighbor)) {
                        const edge = findEdgeElement(nodeIndex, neighbor);
                        if (edge) {
                            edge.classList.add(colorClass);
                        }
                    }
                });
            });
        });
    }
    
    // エッジ要素を探す関数
    function findEdgeElement(source, target) {
        return edgeElements.find(edge => 
            (edge.dataset.source == source && edge.dataset.target == target) || 
            (edge.dataset.source == target && edge.dataset.target == source)
        );
    }
    
    // スリープ関数（アニメーション用）
    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
    
    // アルゴリズムの実行
    async function runAlgorithm() {
        if (isRunning) return;
        
        isRunning = true;
        startBtn.disabled = true;
        
        // すべてのノードとエッジのクラスをリセット
        nodeElements.forEach(node => {
            node.className = 'node';
        });
        
        edgeElements.forEach(edge => {
            edge.className = 'edge';
        });
        
        const algorithm = algorithmSelect.value;
        
        try {
            switch (algorithm) {
                case 'dfs':
                    await runDFS();
                    break;
                case 'bfs':
                    await runBFS();
                    break;
                case 'unionfind':
                    await runUnionFind();
                    break;
            }
        } catch (error) {
            console.error('Algorithm error:', error);
            updateStepInfo(currentStep + 1, `エラーが発生しました: ${error.message}`);
        }
        
        isRunning = false;
        startBtn.disabled = false;
    }
    
    // イベントリスナーの設定
    startBtn.addEventListener('click', runAlgorithm);
    clearBtn.addEventListener('click', () => {
        if (!isRunning) {
            initializeGraph();
        }
    });
    
    // 初期グラフの作成
    initializeGraph();
});