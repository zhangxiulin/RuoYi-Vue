<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="datasourceName">
        <el-input
          v-model="queryParams.datasourceName"
          placeholder="请输入数据源名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="厂商" prop="databaseType">
        <el-select v-model="queryParams.databaseType" placeholder="请选择数据库厂商" clearable size="small">
          <el-option
            v-for="dict in databaseTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="连接池" prop="datasourceType">
        <el-select v-model="queryParams.datasourceType" placeholder="请选择连接池种类" clearable size="small">
          <el-option
            v-for="dict in datasourceTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="启用状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择启用状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['integrator:datasource:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['integrator:datasource:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['integrator:datasource:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['integrator:datasource:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="datasourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="主键ID" align="center" prop="datasourceId" /> -->
      <el-table-column label="数据源名称" align="center" prop="datasourceName" />
      <el-table-column label="数据库厂商" align="center" prop="databaseType" :formatter="databaseTypeFormat" />
      <el-table-column label="连接池种类" align="center" prop="datasourceType" :formatter="datasourceTypeFormat" />
      <el-table-column label="数据源配置" align="center" prop="datasourceOptions" :show-overflow-tooltip=true  />
      <el-table-column label="XA启用" align="center" prop="xaEnabled" :formatter="xaEnabledFormat" />
      <el-table-column label="XA数据源类型" align="center" prop="xaDatasourceType" :formatter="xaDatasourceTypeFormat" />
      <el-table-column label="启用状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip=true  />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['integrator:datasource:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['integrator:datasource:remove']"
          >删除</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-refresh"
            @click="handleSynchDs(scope.row)"
            v-hasPermi="['integrator:datasource:synchDs']"
          >同步</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-refresh"
            @click="handleSynchAtomikos(scope.row)"
            v-hasPermi="['integrator:datasource:synchAtomikos']"
          >同步Atomikos</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改数据源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-tabs tab-position="left" style="height: 400px;">
          <el-tab-pane label="基础配置">
            <el-form-item label="数据源名称" prop="datasourceName">
              <el-input v-model="form.datasourceName" placeholder="请输入数据源名称" />
            </el-form-item>
            <el-form-item label="数据库厂商" prop="databaseType">
              <el-select v-model="form.databaseType" placeholder="请选择数据库厂商">
                <el-option
                  v-for="dict in databaseTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="连接池种类" prop="datasourceType">
              <el-select v-model="form.datasourceType" placeholder="请选择连接池种类" @change="datasouceTypeChange" >
                <el-option
                  v-for="dict in datasourceTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="数据源配置" prop="datasourceOptions">
              <el-input v-model="form.datasourceOptions" type="textarea" placeholder="请输入内容（json格式）" />
            </el-form-item>
            <el-form-item label="启用状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="XA">
            <el-form-item label="XA启用">
              <el-radio-group v-model="form.xaEnabled">
                <el-radio
                  v-for="dict in xaEnabledOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="XA数据源类型" prop="xaDatasourceType">
              <el-select v-model="form.xaDatasourceType" placeholder="请选择XA数据源类型">
                <el-option
                  v-for="dict in xaDatasourceTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="XA数据源配置" prop="xaDatasourceOptions">
              <el-input v-model="form.xaDatasourceOptions" type="textarea" placeholder="请输入内容（json格式）" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDatasource, getDatasource, delDatasource, addDatasource, updateDatasource, exportDatasource, synchDs, synchAtomikos } from "@/api/integrator/datasource";

export default {
  name: "Datasource",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      datasourceNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据源表格数据
      datasourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据库厂商字典
      databaseTypeOptions: [],
      // 连接池种类字典
      datasourceTypeOptions: [],
      // 启用状态字典
      statusOptions: [],
      // XA数据源类型字典
      xaDatasourceTypeOptions: [],
      // XA启用字典
      xaEnabledOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        datasourceName: null,
        databaseType: null,
        datasourceType: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        datasourceName: [
          { required: true, message: "数据源名称不能为空", trigger: "blur" }
        ],
        datasourceType: [
          { required: true, message: "连接池种类不能为空", trigger: "change" }
        ],
        datasourceOptions: [
          { required: true, message: "数据源配置不能为空", trigger: "blur" }
        ],
      },
      // druid配置信息示例
      druidDemo : {
        "driverClassName":"com.mysql.cj.jdbc.Driver",
        "url":"",
        "username":"",
        "password":"",
        "initialSize": 5,
        "minIdle": 10,
        "maxActive": 20,
        "maxWait": 60000,
        "timeBetweenEvictionRunsMillis": 60000,
        "minEvictableIdleTimeMillis": 300000,
        "maxEvictableIdleTimeMillis": 900000,
        "validationQuery": "SELECT 1 FROM DUAL",
        "testWhileIdle": true,
        "testOnBorrow": false,
        "testOnReturn": false,
        "webStatFilter":{"enabled": true}
      },
      hikariDemo : {
        "driverClassName":"com.mysql.cj.jdbc.Driver",
        "url":"",
        "username":"",
        "password":"",
        "autoCommit": true,
        "idleTimeout":30000,
        "poolName":"default",
        "registerMbeans":"",
        "catalog":true,
        "maximumPoolSize":"20",
        "minimumIdle":"5"
      },
      xaDatasourceTypeOptions:[],
    };
  },
  created() {
    this.getList();
    this.getDicts("in_database_type").then(response => {
      this.databaseTypeOptions = response.data;
    });
    this.getDicts("in_datasource_type").then(response => {
      this.datasourceTypeOptions = response.data;
    });
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("in_xa_datasource_type").then(response => {
      this.xaDatasourceTypeOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.xaEnabledOptions = response.data;
    });
  },
  methods: {
    /** 查询数据源列表 */
    getList() {
      this.loading = true;
      listDatasource(this.queryParams).then(response => {
        this.datasourceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 数据库厂商字典翻译
    databaseTypeFormat(row, column) {
      return this.selectDictLabel(this.databaseTypeOptions, row.databaseType);
    },
    // 连接池种类字典翻译
    datasourceTypeFormat(row, column) {
      return this.selectDictLabel(this.datasourceTypeOptions, row.datasourceType);
    },
    // 启用状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // XA启用字典翻译
    xaEnabledFormat(row, column) {
      return this.selectDictLabel(this.xaEnabledOptions, row.xaEnabled);
    },
    // XA数据源类型字典翻译
    xaDatasourceTypeFormat(row, column) {
      return this.selectDictLabel(this.xaDatasourceTypeOptions, row.xaDatasourceType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        datasourceId: null,
        datasourceName: null,
        databaseType: "MYSQL",
        datasourceType: "Druid",
        datasourceOptions: JSON.stringify(this.druidDemo),
        xaEnabled: "Y",
        xaDatasourceType: null,
        xaDatasourceOptions: null,
        status: "0",
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.datasourceId)
      this.datasourceNames = selection.map(item => item.datasourceName)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const datasourceId = row.datasourceId || this.ids
      getDatasource(datasourceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数据源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.datasourceId != null) {
            updateDatasource(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDatasource(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const datasourceIds = row.datasourceId || this.ids;
      const datasourceNames = row.datasourceName || this.datasourceNames;
      this.$confirm('是否确认删除数据源编号为"' + datasourceNames + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDatasource(datasourceIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有数据源数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportDatasource(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },

    /**切换数据源厂商类型**/
    datasouceTypeChange(item){
      if (item == "Druid") {
        this.form.datasourceOptions = JSON.stringify(this.druidDemo);
      }else if (item == "HikariCP"){
        this.form.datasourceOptions = JSON.stringify(this.hikariDemo);
      }
    },

    /** 同步数据库操作 */
    handleSynchDs(row) {
      const datasourceName = row.datasourceName;
      const datasourceId = row.datasourceId;
      this.$confirm('确认要强制同步"' + datasourceName + '"数据源吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return synchDs(datasourceId);
      }).then(() => {
        this.msgSuccess("同步成功");
      })
    },

    /** 同步数据库操作 */
    handleSynchAtomikos(row) {
      const datasourceName = row.datasourceName;
      const datasourceId = row.datasourceId;
      // 判断连接池是否支持XA
      const xaDatasourceType = row.xaDatasourceType;
      if (!this.xaDatasourceTypeOptions.some(function(item){return item.dictValue == xaDatasourceType})){
        this.msgError("连接池类型["+xaDatasourceType+"]不支持XA");
        return;
      }

      this.$confirm('确认要强制同步"' + datasourceName + '"Atomikos数据源吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return synchAtomikos(datasourceId);
      }).then(() => {
        this.msgSuccess("同步成功");
      })
    }
  }
};
</script>
