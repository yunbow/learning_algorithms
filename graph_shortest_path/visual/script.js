document.addEventListener('DOMContentLoaded', () => {
    const graphContainer = document.getElementById('graph-container');
    const algorithmSelect = document.getElementById('algorithm-select');
    const runButton = document.getElementById('run-button');
    const clearButton = document.getElementById('clear-button');
    const stepInfo = document.getElementById('step-info');
    const actionInfo = document.getElementById('action-info');

    // グラフデータ (ノードの位置、エッジ、重み)
    // ノード: id, name, x (%), y (%)
    // エッジ: from, to, weight
    const graphData = {
        nodes: [
            { id: 'A', name: 'A', x: 10, y: 50 },
            { id: 'B', name: 'B', x: 35, y: 20 },
            { id: 'C', name: 'C', x: 35, y: 80 },
            { id: 'D', name: 'D', x: 65, y: 20 },
            { id: 'E', name: 'E', x: 65, y: 80 },
            { id: 'F', name: 'F', x: 90, y: 50 },
        ],
        edges: [
            { from: 'A', to: 'B', weight: 7, id: 'AB' },
            { from: 'A', to: 'C', weight: 3, id: 'AC' },
            { from: 'B', to: 'A', weight: 7, id: 'BA' }, // 双方向の場合
            { from: 'C', to: 'A', weight: 3, id: 'CA' }, // 双方向の場合
            { from: 'B', to: 'D', weight: 2, id: 'BD' },
            { from: 'D', to: 'B', weight: 2, id: 'DB' },
            { from: 'B', to: 'C', weight: 1, id: 'BC' },
            { from: 'C', to: 'B', weight: 1, id: 'CB' },
            { from: 'C', to: 'E', weight: 5, id: 'CE' },
            { from: 'E', to: 'C', weight: 5, id: 'EC' },
            { from: 'D', to: 'E', weight: 2, id: 'DE' },
            { from: 'E', to: 'D', weight: 2, id: 'ED' },
            { from: 'D', to: 'F', weight: 6, id: 'DF' },
            { from: 'F', to: 'D', weight: 6, id: 'FD' },
            { from: 'E', to: 'F', weight: 8, id: 'EF' },
            { from: 'F', to: 'E', weight: 8, id: 'FE' },
        ],
        startNode: 'A',
        endNode: 'F' // 最短経路の終点ノード (オプション)
    };

    let animationSteps = [];
    let currentStep = 0;
    let isAnimating = false;
    const ANIMATION_DELAY = 1500; // ms

    // グラフの初期描画
    function drawGraph() {
        graphContainer.innerHTML = ''; // 既存の描画をクリア

        // ノードを描画
        graphData.nodes.forEach(node => {
            const nodeEl = document.createElement('div');
            nodeEl.classList.add('node');
            nodeEl.id = `node-${node.id}`;
            nodeEl.style.left = `${node.x}%`;
            nodeEl.style.top = `${node.y}%`;
            nodeEl.textContent = node.name;
            if (node.id === graphData.startNode) nodeEl.classList.add('start');
            if (node.id === graphData.endNode) nodeEl.classList.add('end');
            graphContainer.appendChild(nodeEl);
        });

        // エッジを描画
        const drawnEdges = new Set(); // 重複描画を防ぐ (A-BとB-Aが同じ線にならないように)
        graphData.edges.forEach(edge => {
            const pair = [edge.from, edge.to].sort().join('-');
            if (drawnEdges.has(pair)) return;
            drawnEdges.add(pair);

            const nodeFrom = graphData.nodes.find(n => n.id === edge.from);
            const nodeTo = graphData.nodes.find(n => n.id === edge.to);

            if (!nodeFrom || !nodeTo) return;

            const nodeFromEl = document.getElementById(`node-${nodeFrom.id}`);
            const nodeToEl = document.getElementById(`node-${nodeTo.id}`);

            // ノードの中心座標を取得
            const x1 = nodeFrom.x + (nodeFromEl.offsetWidth / graphContainer.offsetWidth * 100) / 2;
            const y1 = nodeFrom.y + (nodeFromEl.offsetHeight / graphContainer.offsetHeight * 100) / 2;
            const x2 = nodeTo.x + (nodeToEl.offsetWidth / graphContainer.offsetWidth * 100) / 2;
            const y2 = nodeTo.y + (nodeToEl.offsetHeight / graphContainer.offsetHeight * 100) / 2;

            const length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) * (graphContainer.offsetWidth / 100);
            const angle = Math.atan2(y2 - y1, x2 - x1) * (180 / Math.PI);

            const edgeEl = document.createElement('div');
            edgeEl.classList.add('edge');
            edgeEl.id = `edge-${edge.id}`; // または edge-${edge.from}-${edge.to}
            edgeEl.style.left = `${x1}%`;
            edgeEl.style.top = `${y1}%`;
            edgeEl.style.width = `${length}px`;
            edgeEl.style.transform = `rotate(${angle}deg)`;
            graphContainer.appendChild(edgeEl);

            // 重みをエッジの中間点に表示
            const weightEl = document.createElement('div');
            weightEl.classList.add('edge-weight');
            weightEl.textContent = edge.weight;
            // 重みの位置を調整 (線の中心、少しオフセット)
            const midX = (x1 + x2) / 2;
            const midY = (y1 + y2) / 2;
            weightEl.style.left = `${midX}%`;
            weightEl.style.top = `${midY}%`;
            weightEl.style.transform = `translate(-50%, -50%) rotate(${-angle}deg)`; // 文字が回転しないように
             if (angle > 90 || angle < -90) { // 上下反転する場合の補正
                weightEl.style.transform = `translate(-50%, -50%) rotate(${-angle+180}deg)`;
            }
            graphContainer.appendChild(weightEl);
        });
        setInfo("初期状態", "グラフが描画されました。アルゴリズムを選択して実行してください。");
    }

    function setInfo(stepText, actionText) {
        stepInfo.textContent = stepText;
        actionInfo.textContent = actionText;
    }

    function highlightNode(nodeId, type = 'visited') { // type: visited, current, path
        const nodeEl = document.getElementById(`node-${nodeId}`);
        if (nodeEl) {
            nodeEl.classList.remove('visited', 'current', 'path'); // Reset previous types
            nodeEl.classList.add(type);
        }
    }

    function highlightEdge(fromNodeId, toNodeId, type = 'highlighted') { // type: highlighted, path
        const edgeId1 = `edge-${fromNodeId}-${toNodeId}`;
        const edgeId2 = `edge-${toNodeId}-${fromNodeId}`;
        const edgeData1 = graphData.edges.find(e => e.id === `${fromNodeId}${toNodeId}`);
        const edgeData2 = graphData.edges.find(e => e.id === `${toNodeId}${fromNodeId}`);


        let edgeEl = document.getElementById(edgeData1 ? `edge-${edgeData1.id}` : edgeId1);
        if (!edgeEl && edgeData2) {
             edgeEl = document.getElementById(`edge-${edgeData2.id}`);
        }


        if (edgeEl) {
            edgeEl.classList.remove('highlighted', 'path'); // Reset previous types
            edgeEl.classList.add(type);
        }
    }

    function clearHighlights() {
        graphData.nodes.forEach(node => {
            const nodeEl = document.getElementById(`node-${node.id}`);
            if (nodeEl) {
                nodeEl.classList.remove('visited', 'current', 'path');
                if (node.id === graphData.startNode) nodeEl.classList.add('start');
                else if (node.id === graphData.endNode) nodeEl.classList.add('end');
                else nodeEl.className = 'node'; // 基本クラスのみ
            }
        });
        const drawnEdges = new Set();
        graphData.edges.forEach(edge => {
            const pair = [edge.from, edge.to].sort().join('-');
            if (drawnEdges.has(pair)) return;
            drawnEdges.add(pair);

            const edgeEl = document.getElementById(`edge-${edge.id}`) || document.getElementById(`edge-${edge.to}${edge.from}`);
            if (edgeEl) {
                 edgeEl.className = 'edge'; // 基本クラスのみ
            }
        });
    }


    // ダイクストラ法の実装
    function dijkstra(startNodeId, endNodeId) {
        animationSteps = [];
        const distances = {};
        const predecessors = {};
        const visited = new Set();
        const pq = new PriorityQueue(); // 簡易的な優先度付きキュー

        // 初期化
        graphData.nodes.forEach(node => {
            distances[node.id] = Infinity;
            predecessors[node.id] = null;
        });
        distances[startNodeId] = 0;
        pq.enqueue(startNodeId, 0);

        animationSteps.push({
            type: 'init',
            distances: { ...distances },
            message: `初期化: ${startNodeId} の距離を0、他を無限大に設定。`
        });

        let stepCounter = 1;

        while (!pq.isEmpty()) {
            const { element: currentNodeId, priority: currentDistance } = pq.dequeue();

            if (visited.has(currentNodeId)) continue;
            visited.add(currentNodeId);

            animationSteps.push({
                type: 'visit_node',
                nodeId: currentNodeId,
                distance: currentDistance,
                message: `ステップ ${stepCounter++}: ノード ${currentNodeId} を処理 (現在距離: ${currentDistance})。`
            });

            if (currentNodeId === endNodeId) {
                 animationSteps.push({
                    type: 'target_found',
                    nodeId: currentNodeId,
                    message: `目標ノード ${endNodeId} に到達しました。`
                });
                break; // 終点に到達したら終了
            }

            // 隣接ノードの処理
            graphData.edges
                .filter(edge => edge.from === currentNodeId)
                .forEach(edge => {
                    const neighborNodeId = edge.to;
                    const weight = edge.weight;
                    const distanceThroughCurrent = currentDistance + weight;

                    animationSteps.push({
                        type: 'check_edge',
                        from: currentNodeId,
                        to: neighborNodeId,
                        weight: weight,
                        newDist: distanceThroughCurrent,
                        oldDist: distances[neighborNodeId],
                        message: `  エッジ ${currentNodeId} -> ${neighborNodeId} (重み ${weight}) を確認。`
                    });

                    if (distanceThroughCurrent < distances[neighborNodeId]) {
                        distances[neighborNodeId] = distanceThroughCurrent;
                        predecessors[neighborNodeId] = currentNodeId;
                        pq.enqueue(neighborNodeId, distanceThroughCurrent);
                        animationSteps.push({
                            type: 'update_distance',
                            nodeId: neighborNodeId,
                            newDistance: distanceThroughCurrent,
                            predecessor: currentNodeId,
                            message: `    ノード ${neighborNodeId} の距離を ${distanceThroughCurrent} に更新 (経由: ${currentNodeId})。`
                        });
                    } else {
                         animationSteps.push({
                            type: 'no_update',
                            nodeId: neighborNodeId,
                            message: `    ノード ${neighborNodeId} の距離は更新されません (${distances[neighborNodeId]} <= ${distanceThroughCurrent})。`
                        });
                    }
                });
        }

        // 最短経路の再構築 (終点がある場合)
        if (endNodeId && distances[endNodeId] !== Infinity) {
            const path = [];
            let current = endNodeId;
            while (current) {
                path.unshift(current);
                if (predecessors[current] && current !== startNodeId) {
                     animationSteps.push({
                        type: 'path_reconstruct_edge',
                        from: predecessors[current],
                        to: current,
                        message: `経路復元: ${predecessors[current]} -> ${current}`
                    });
                }
                current = predecessors[current];
            }
             animationSteps.push({
                type: 'path_reconstruct_done',
                path: path,
                totalDistance: distances[endNodeId],
                message: `最短経路: ${path.join(' -> ')} (合計距離: ${distances[endNodeId]})`
            });
        } else if (endNodeId) {
            animationSteps.push({
                type: 'target_not_reachable',
                message: `目標ノード ${endNodeId} には到達できませんでした。`
            });
        }

        return animationSteps;
    }

    // 簡易的な優先度付きキューの実装
    class PriorityQueue {
        constructor() {
            this.items = [];
        }
        enqueue(element, priority) {
            const queueElement = { element, priority };
            let added = false;
            for (let i = 0; i < this.items.length; i++) {
                if (this.items[i].priority > queueElement.priority) {
                    this.items.splice(i, 0, queueElement);
                    added = true;
                    break;
                }
            }
            if (!added) {
                this.items.push(queueElement);
            }
        }
        dequeue() {
            if (this.isEmpty()) return null;
            return this.items.shift();
        }
        isEmpty() {
            return this.items.length === 0;
        }
    }

    async function runAnimation() {
        if (isAnimating) return;
        isAnimating = true;
        runButton.disabled = true;
        clearButton.disabled = true;
        clearHighlights(); // 前のアニメーションのハイライトをクリア

        const selectedAlgorithm = algorithmSelect.value;
        if (selectedAlgorithm === 'dijkstra') {
            animationSteps = dijkstra(graphData.startNode, graphData.endNode);
        } else {
            setInfo("エラー", "選択されたアルゴリズムはまだ実装されていません。");
            isAnimating = false;
            runButton.disabled = false;
            clearButton.disabled = false;
            return;
        }

        currentStep = 0;
        await playNextStep();
    }

    async function playNextStep() {
        if (currentStep >= animationSteps.length) {
            isAnimating = false;
            runButton.disabled = false;
            clearButton.disabled = false;
            // 最終ステップで経路全体をハイライトする処理はここで行うか、
            // dijkstra の path_reconstruct_done で行う
            const finalStep = animationSteps[animationSteps.length -1];
            if (finalStep && finalStep.type === 'path_reconstruct_done' && finalStep.path) {
                highlightShortestPath(finalStep.path);
                setInfo(`完了 (ステップ ${currentStep}/${animationSteps.length})`, finalStep.message);
            } else if (finalStep && finalStep.type === 'target_not_reachable') {
                 setInfo(`完了 (ステップ ${currentStep}/${animationSteps.length})`, finalStep.message);
            } else if (finalStep) {
                setInfo(`完了 (ステップ ${currentStep}/${animationSteps.length})`, finalStep.message || "アニメーション終了");
            }

            return;
        }

        const step = animationSteps[currentStep];
        setInfo(`ステップ ${currentStep + 1}/${animationSteps.length}`, step.message);

        // アニメーション処理
        clearNodeEdgeHighlights(); // 毎回クリアして現在の状態のみ表示

        if (step.type === 'init') {
            // 初期化は特に視覚的変更なし（メッセージのみ）
        } else if (step.type === 'visit_node') {
            highlightNode(step.nodeId, 'current'); // 現在処理中のノード
            setTimeout(() => highlightNode(step.nodeId, 'visited'), ANIMATION_DELAY / 2); // 少し遅れて訪問済みに
        } else if (step.type === 'check_edge') {
            highlightNode(step.from, 'current');
            highlightEdge(step.from, step.to, 'highlighted');
        } else if (step.type === 'update_distance') {
            highlightNode(step.predecessor, 'visited'); // 前のノードは訪問済み
            highlightNode(step.nodeId, 'current'); // 更新対象ノード
            highlightEdge(step.predecessor, step.nodeId, 'highlighted');
        } else if (step.type === 'no_update') {
            //特にハイライト変更なし、メッセージのみ
        } else if (step.type === 'target_found') {
            highlightNode(step.nodeId, 'end');
        } else if (step.type === 'path_reconstruct_edge') {
            highlightNode(step.from, 'path');
            highlightNode(step.to, 'path');
            highlightEdge(step.from, step.to, 'path');
        } else if (step.type === 'path_reconstruct_done') {
            // 経路復元完了時は、最終的なパスを強調表示
            highlightShortestPath(step.path);
        }


        currentStep++;
        await new Promise(resolve => setTimeout(resolve, ANIMATION_DELAY));
        playNextStep();
    }

    function clearNodeEdgeHighlights() {
        // 現在のノードや一時的なエッジハイライトのみクリア
        graphData.nodes.forEach(node => {
            const nodeEl = document.getElementById(`node-${node.id}`);
            if (nodeEl && !nodeEl.classList.contains('path') && !nodeEl.classList.contains('start') && !nodeEl.classList.contains('end') && !nodeEl.classList.contains('visited')) {
                nodeEl.classList.remove('current');
            }
             if (nodeEl && nodeEl.classList.contains('current') && !nodeEl.classList.contains('visited')){
                 // current のまま残す場合がある
            }
        });
        const drawnEdges = new Set();
        graphData.edges.forEach(edge => {
            const pair = [edge.from, edge.to].sort().join('-');
            if (drawnEdges.has(pair)) return;
            drawnEdges.add(pair);
            const edgeEl = document.getElementById(`edge-${edge.id}`) || document.getElementById(`edge-${edge.to}${edge.from}`);
            if (edgeEl && !edgeEl.classList.contains('path')) {
                edgeEl.classList.remove('highlighted');
            }
        });
    }


    function highlightShortestPath(pathArray) {
        if (!pathArray || pathArray.length === 0) return;
        for (let i = 0; i < pathArray.length; i++) {
            highlightNode(pathArray[i], 'path');
            if (i > 0) {
                highlightEdge(pathArray[i-1], pathArray[i], 'path');
            }
        }
        // Start and End nodes might need special styling if they are part of the path
        const startNodeEl = document.getElementById(`node-${graphData.startNode}`);
        const endNodeEl = document.getElementById(`node-${graphData.endNode}`);
        if (startNodeEl && pathArray.includes(graphData.startNode)) startNodeEl.classList.add('path', 'start');
        if (endNodeEl && pathArray.includes(graphData.endNode)) endNodeEl.classList.add('path', 'end');

    }


    function clearAll() {
        isAnimating = false; // アニメーション中なら停止
        currentStep = 0;
        animationSteps = [];
        clearHighlights();
        drawGraph(); // グラフを再描画して初期状態に戻す
        setInfo("クリア", "グラフが初期化されました。");
        runButton.disabled = false;
        clearButton.disabled = false;
    }

    // イベントリスナー
    runButton.addEventListener('click', runAnimation);
    clearButton.addEventListener('click', clearAll);

    // 初期化
    drawGraph();
});