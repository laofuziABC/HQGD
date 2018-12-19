
Gef.ns('Gef.org');

Gef.ORG_URL = '../../workspace/org!getAllTree.do';

Gef.org.CheckboxTreeNodeUI = Ext.extend(Ext.tree.CheckboxNodeUI, {
    checkParent: Ext.emptyFn,
    checkChild: Ext.emptyFn
});

Gef.org.OrgField = Ext.extend(Ext.form.TriggerField, {
    initComponent: function() {
        this.readOnly = true;
        Gef.org.OrgField.superclass.initComponent.call(this);

        this.addEvents('select');
    },

    onTriggerClick: function() {
        this.showWindow();
    },

    showWindow: function() {
        this.getWindow().show();
        var value = this.getValue();
        var array = value.split(',');
        this.orgTree.cleanCheck();
        this.orgTree.setChecked(array);

        //Gef.activeEditor.disable();
    },

    hideWindow: function() {
        this.getWindow().hide();

        //Gef.activeEditor.enable();
    },

    getWindow: function() {
        if (!this.orgWindow) {
            this.orgWindow = this.createWindow();
        }
        return this.orgWindow;
    },

    createWindow: function() {
        var tree = new Ext.tree.TreePanel({
            autoScroll: true,
            loader: new Ext.tree.CustomUITreeLoader({
                dataUrl: Gef.ORG_URL,
                baseAttr: {
                    uiProvider: Gef.org.CheckboxTreeNodeUI
                }
            }),
            enableDD: false,
            containerScroll: true,
            rootUIProvider: Gef.org.CheckboxTreeNodeUI,
            selModel: new Ext.tree.CheckNodeMultiSelectionModel(),
            rootVisible: false
        });
        tree.getLoader().on('load', function(o, node) {
            if (node.isRoot) {
                tree.expandAll();
            }
        });
        var root = new Ext.tree.AsyncTreeNode({
            text: 'root',
            draggable: false
        });
        tree.setRootNode(root);

        tree.getChecked = function(node) {
            var checked = [], i;
            if (typeof node == 'undefined') {
                //node = this.rootVisible ? this.getRootNode() : this.getRootNode().firstChild;
                node = this.getRootNode();
            } else if (node.ui.checkboxImg.className == 'x-tree-node-checkbox-all') {
                checked.push(node.id);
            }
            if (!node.isLeaf()) {
                for(var i = 0; i < node.childNodes.length; i++) {
                    checked = checked.concat(this.getChecked(node.childNodes[i]));
                }
            }
            return checked;
        };
        tree.setChecked = function(array) {
            for (var i = array.length - 1; i >= 0; i--) {
                var n = this.getNodeById(array[i]);
                if (n && !n.getUI().checked()) {
                    n.getUI().check();
                }
            }
        };
        tree.cleanCheck = function(node) {
            if (typeof node == 'undefined') {
                node = this.rootVisible ? this.getRootNode() : this.getRootNode().firstChild;
            }
            if (node) {
                node.ui.checkboxImg.className = 'x-tree-node-checkbox-none';
                node.attributes.checked = false;
                if (!node.isLeaf()) {
                    for(var i = 0; i < node.childNodes.length; i++) {
                        this.cleanCheck(node.childNodes[i]);
                    }
                }
            }

        };

        this.orgTree = tree;

        var win = new Ext.Window({
            title: 'Org',
            layout: 'fit',
            height: 300,
            width: 400,
            closeAction: 'hide',
            modal: true,
            items: [tree],
            buttons: [{
                text: '项目发起人',
                handler: this.selectOwner,
                scope: this
            }, {
                text: '确定',
                handler: this.submit,
                scope: this
            }, {
                text: '取消',
                handler: this.hideWindow,
                scope: this
            }, {
                text: '刷新',
                handler: function() {
                    tree.root.reload();
                },
                scope: this
            }]
        });

        win.field = this;
        return win;
    },

    submit: function() {
        var value = '';
        var array = this.orgTree.getChecked();
        for (var i = 0; i < array.length; i++) {
            value += array[i];
            if (i != array.length - 1) {
                value += ',';
            }
        }
        this.setValue(value);
        this.hideWindow();
        //this.propertyPanel.propertyGrid.propStore.setValue("assignee", value);
        this.fireEvent('select', this);
    },

    selectOwner: function() {
        this.setValue("项目发起人");
        this.hideWindow();
        this.fireEvent('select', this);
    },

    refreshTree: function() {
    }
});

Ext.reg('orgfield', Gef.org.OrgField);

