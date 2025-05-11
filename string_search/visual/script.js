document.addEventListener('DOMContentLoaded', () => {
    // DOM要素
    const textInput = document.getElementById('text');
    const patternInput = document.getElementById('pattern');
    const algorithmSelect = document.getElementById('algorithm');
    const startBtn = document.getElementById('startBtn');
    const clearBtn = document.getElementById('clearBtn');
    const textDisplay = document.getElementById('text-display');
    const patternDisplay = document.getElementById('pattern-display');
    const stepCounter = document.getElementById('step-counter');
    const descriptionText = document.getElementById('description-text');

    // アニメーション関連の変数
    let animationInProgress = false;
    let animationSpeed = 600; // ミリ秒
    let currentStep = 0;
    let animationSteps = [];
    let animationTimeout;

    // イベントリスナー
    startBtn.addEventListener('click', startAnimation);
    clearBtn.addEventListener('click', resetVisualization);
    textInput.addEventListener('input', updateDisplay);
    patternInput.addEventListener('input', updateDisplay);

    // 初期表示
    updateDisplay();

    // 表示を更新する関数
    function updateDisplay() {
        const text = textInput.value;
        const pattern = patternInput.value;

        // テキスト表示
        textDisplay.innerHTML = '';
        
        // インデックス表示
        const indexRow = document.createElement('div');
        indexRow.className = 'index-row';
        
        for (let i = 0; i < text.length; i++) {
            const indexSpan = document.createElement('span');
            indexSpan.className = 'index';
            indexSpan.textContent = i;
            indexRow.appendChild(indexSpan);
        }
        textDisplay.appendChild(indexRow);
        
        // 文字表示
        const charRow = document.createElement('div');
        charRow.className = 'char-row';
        
        for (let i = 0; i < text.length; i++) {
            const charSpan = document.createElement('span');
            charSpan.className = 'char text-char';
            charSpan.textContent = text[i];
            charSpan.dataset.index = i;
            charRow.appendChild(charSpan);
        }
        textDisplay.appendChild(charRow);

        // パターン表示をクリア
        patternDisplay.innerHTML = '';
    }

    // アニメーションを開始する関数
    function startAnimation() {
        if (animationInProgress) return;

        const text = textInput.value;
        const pattern = patternInput.value;
        const algorithm = algorithmSelect.value;

        if (!text || !pattern) {
            descriptionText.textContent = 'テキストとパターンを入力してください。';
            return;
        }

        animationInProgress = true;
        currentStep = 0;
        patternDisplay.innerHTML = '';
        stepCounter.textContent = '0';
        descriptionText.textContent = 'アニメーションを開始します...';

        // アルゴリズムに基づいてアニメーションステップを生成
        switch (algorithm) {
            case 'naive':
                animationSteps = generateNaiveSteps(text, pattern);
                break;
            case 'kmp':
                animationSteps = generateKMPSteps(text, pattern);
                break;
            case 'bm':
                animationSteps = generateBMSteps(text, pattern);
                break;
        }

        // 最初のステップを実行
        executeNextStep();
    }

    // 次のアニメーションステップを実行
    function executeNextStep() {
        if (currentStep >= animationSteps.length) {
            animationInProgress = false;
            return;
        }

        const step = animationSteps[currentStep];
        
        // ステップカウンターを更新
        stepCounter.textContent = (currentStep + 1).toString();
        
        // 説明テキストを更新
        descriptionText.textContent = step.description;
        
        // パターン表示を更新
        updatePatternDisplay(step.patternPosition);
        
        // 文字のハイライトを更新
        updateHighlights(step.highlights);
        
        // 次のステップに進む
        currentStep++;
        
        // タイムアウトを設定
        animationTimeout = setTimeout(executeNextStep, animationSpeed);
    }

    // パターン表示を更新
    function updatePatternDisplay(position) {
        const pattern = patternInput.value;
        const text = textInput.value;
        
        patternDisplay.innerHTML = '';
        
        // スペースを追加してパターンを位置合わせ
        if (position > 0) {
            const spacer = document.createElement('span');
            spacer.style.marginLeft = `${position * 34}px`; // 文字幅+マージン
            spacer.style.display = 'inline-block';
            patternDisplay.appendChild(spacer);
        }
        
        // パターンの文字を追加
        for (let i = 0; i < pattern.length; i++) {
            const charSpan = document.createElement('span');
            charSpan.className = 'char pattern-char';
            charSpan.textContent = pattern[i];
            patternDisplay.appendChild(charSpan);
        }
    }

    // ハイライトを更新
    function updateHighlights(highlights) {
        // すべてのハイライトをリセット
        const chars = document.querySelectorAll('.text-char');
        chars.forEach(char => {
            char.classList.remove('highlight', 'match', 'mismatch');
        });
        
        const patternChars = document.querySelectorAll('.pattern-char');
        patternChars.forEach(char => {
            char.classList.remove('highlight', 'match', 'mismatch');
        });
        
        // 新しいハイライトを適用
        if (highlights) {
            highlights.forEach(h => {
                const index = h.index;
                const status = h.status;
                const charElements = document.querySelectorAll(`.text-char[data-index="${index}"]`);
                
                if (charElements.length > 0) {
                    const charElement = charElements[0];
                    charElement.classList.add('highlight');
                    if (status === 'match') charElement.classList.add('match');
                    if (status === 'mismatch') charElement.classList.add('mismatch');
                }
                
                // パターンの文字もハイライト
                if (h.patternIndex !== undefined) {
                    const patternChars = document.querySelectorAll('.pattern-char');
                    if (h.patternIndex < patternChars.length) {
                        const patternChar = patternChars[h.patternIndex];
                        patternChar.classList.add('highlight');
                        if (status === 'match') patternChar.classList.add('match');
                        if (status === 'mismatch') patternChar.classList.add('mismatch');
                    }
                }
            });
        }
    }

    // 素朴な検索アルゴリズムのステップを生成
    function generateNaiveSteps(text, pattern) {
        const steps = [];
        let found = false;
        
        steps.push({
            description: '素朴な検索アルゴリズムでは、テキスト内の各位置でパターンと比較していきます。',
            patternPosition: 0,
            highlights: []
        });
        
        for (let i = 0; i <= text.length - pattern.length && !found; i++) {
            steps.push({
                description: `位置 ${i} からパターンの比較を開始します。`,
                patternPosition: i,
                highlights: []
            });
            
            let j;
            for (j = 0; j < pattern.length; j++) {
                const textIndex = i + j;
                const isMatch = text[textIndex] === pattern[j];
                
                steps.push({
                    description: `テキスト位置 ${textIndex} (${text[textIndex]}) とパターン位置 ${j} (${pattern[j]}) を比較: ${isMatch ? '一致' : '不一致'}`,
                    patternPosition: i,
                    highlights: [{
                        index: textIndex,
                        status: isMatch ? 'match' : 'mismatch',
                        patternIndex: j
                    }]
                });
                
                if (!isMatch) break;
            }
            
            if (j === pattern.length) {
                steps.push({
                    description: `パターンが見つかりました！位置 ${i} でマッチしています。`,
                    patternPosition: i,
                    highlights: Array.from({ length: pattern.length }, (_, idx) => ({
                        index: i + idx,
                        status: 'match',
                        patternIndex: idx
                    }))
                });
                found = true;
            } else {
                steps.push({
                    description: `位置 ${i} ではマッチしませんでした。次の位置に移動します。`,
                    patternPosition: i + 1,
                    highlights: []
                });
            }
        }
        
        if (!found) {
            steps.push({
                description: 'パターンはテキスト内に見つかりませんでした。',
                patternPosition: text.length,
                highlights: []
            });
        }
        
        return steps;
    }

    // KMP法のステップを生成
    function generateKMPSteps(text, pattern) {
        const steps = [];
        
        steps.push({
            description: 'KMP法では、まず部分一致テーブルを作成します。',
            patternPosition: 0,
            highlights: []
        });
        
        // 部分一致テーブルの作成
        const table = computeKMPTable(pattern);
        
        steps.push({
            description: `部分一致テーブル: [${table.join(', ')}]`,
            patternPosition: 0,
            highlights: []
        });
        
        steps.push({
            description: 'テキストとパターンの比較を開始します。',
            patternPosition: 0,
            highlights: []
        });
        
        let i = 0; // テキストインデックス
        let j = 0; // パターンインデックス
        let found = false;
        
        while (i < text.length && !found) {
            const isMatch = text[i] === pattern[j];
            
            steps.push({
                description: `テキスト位置 ${i} (${text[i]}) とパターン位置 ${j} (${pattern[j]}) を比較: ${isMatch ? '一致' : '不一致'}`,
                patternPosition: i - j,
                highlights: [{
                    index: i,
                    status: isMatch ? 'match' : 'mismatch',
                    patternIndex: j
                }]
            });
            
            if (isMatch) {
                i++;
                j++;
                
                if (j === pattern.length) {
                    const matchPos = i - j;
                    steps.push({
                        description: `パターンが見つかりました！位置 ${matchPos} でマッチしています。`,
                        patternPosition: matchPos,
                        highlights: Array.from({ length: pattern.length }, (_, idx) => ({
                            index: matchPos + idx,
                            status: 'match',
                            patternIndex: idx
                        }))
                    });
                    found = true;
                }
            } else {
                if (j > 0) {
                    const newJ = table[j - 1];
                    steps.push({
                        description: `不一致が発生しました。部分一致テーブルから次の位置を取得: j = ${newJ}`,
                        patternPosition: i - newJ,
                        highlights: []
                    });
                    j = newJ;
                } else {
                    steps.push({
                        description: `不一致が発生し、パターンの先頭でマッチしませんでした。テキストを1文字進めます。`,
                        patternPosition: i + 1,
                        highlights: []
                    });
                    i++;
                }
            }
        }
        
        if (!found) {
            steps.push({
                description: 'パターンはテキスト内に見つかりませんでした。',
                patternPosition: text.length,
                highlights: []
            });
        }
        
        return steps;
    }

    // KMP法の部分一致テーブルを計算
    function computeKMPTable(pattern) {
        const table = new Array(pattern.length).fill(0);
        let len = 0;
        let i = 1;
        
        while (i < pattern.length) {
            if (pattern[i] === pattern[len]) {
                len++;
                table[i] = len;
                i++;
            } else {
                if (len > 0) {
                    len = table[len - 1];
                } else {
                    table[i] = 0;
                    i++;
                }
            }
        }
        
        return table;
    }

    // Boyer-Moore法のステップを生成
    function generateBMSteps(text, pattern) {
        const steps = [];
        
        steps.push({
            description: 'Boyer-Moore法では、まずテキストの最後の文字からパターンの最後の文字を比較していきます。',
            patternPosition: 0,
            highlights: []
        });
        
        // 悪文字表の作成
        const badChar = buildBadCharTable(pattern);
        
        steps.push({
            description: 'まず悪文字表を作成します。',
            patternPosition: 0,
            highlights: []
        });
        
        // 検索処理
        let i = 0; // テキストの位置
        let found = false;
        
        while (i <= text.length - pattern.length && !found) {
            steps.push({
                description: `パターンの位置を ${i} に設定します。`,
                patternPosition: i,
                highlights: []
            });
            
            let j = pattern.length - 1;
            
            while (j >= 0) {
                const textChar = text[i + j];
                const patternChar = pattern[j];
                const isMatch = textChar === patternChar;
                
                steps.push({
                    description: `テキスト位置 ${i + j} (${textChar}) とパターン位置 ${j} (${patternChar}) を比較: ${isMatch ? '一致' : '不一致'}`,
                    patternPosition: i,
                    highlights: [{
                        index: i + j,
                        status: isMatch ? 'match' : 'mismatch',
                        patternIndex: j
                    }]
                });
                
                if (!isMatch) break;
                j--;
            }
            
            if (j < 0) {
                steps.push({
                    description: `パターンが見つかりました！位置 ${i} でマッチしています。`,
                    patternPosition: i,
                    highlights: Array.from({ length: pattern.length }, (_, idx) => ({
                        index: i + idx,
                        status: 'match',
                        patternIndex: idx
                    }))
                });
                found = true;
            } else {
                // 悪文字ルールによるスキップ
                const shift = Math.max(1, j - badChar[text.charCodeAt(i + j)] || j + 1);
                
                steps.push({
                    description: `不一致が発生しました。悪文字ルールにより ${shift} 文字スキップします。`,
                    patternPosition: i,
                    highlights: [{
                        index: i + j,
                        status: 'mismatch',
                        patternIndex: j
                    }]
                });
                
                i += shift;
            }
        }
        
        if (!found) {
            steps.push({
                description: 'パターンはテキスト内に見つかりませんでした。',
                patternPosition: text.length,
                highlights: []
            });
        }
        
        return steps;
    }

    // 悪文字表の作成
    function buildBadCharTable(pattern) {
        const table = {};
        
        for (let i = 0; i < pattern.length - 1; i++) {
            table[pattern.charCodeAt(i)] = i;
        }
        
        return table;
    }

    // 視覚化をリセット
    function resetVisualization() {
        // アニメーションをクリア
        if (animationTimeout) {
            clearTimeout(animationTimeout);
        }
        
        animationInProgress = false;
        currentStep = 0;
        animationSteps = [];
        
        // 表示をリセット
        updateDisplay();
        patternDisplay.innerHTML = '';
        stepCounter.textContent = '0';
        descriptionText.textContent = 'アルゴリズムを選択して「アニメーション実行」ボタンを押してください。';
    }
});