<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="功能名称" prop="forwardName">
        <el-input
          v-model="queryParams.forwardName"
          placeholder="请输入功能名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="转发编号" prop="forwardCode">
        <el-input
          v-model="queryParams.forwardCode"
          placeholder="请输入转发编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="转发类型" prop="forwardType">
        <el-select v-model="queryParams.forwardType" placeholder="请选择转发类型" clearable size="small">
          <el-option
            v-for="dict in forwardTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="转发协议" prop="forwardProtocol">
        <el-select v-model="queryParams.forwardProtocol" placeholder="请选择转发协议" clearable size="small">
          <el-option
            v-for="dict in forwardProtocolOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="URL" prop="forwardUrl">
        <el-input
          v-model="queryParams.forwardUrl"
          placeholder="请输入转发URL"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据源" prop="forwardDatasource">
        <el-input
          v-model="queryParams.forwardDatasource"
          placeholder="请输入数据源"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['integrator:forwardInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['integrator:forwardInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['integrator:forwardInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['integrator:forwardInfo:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="forwardInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="主键ID" align="center" prop="infoId" /> -->
      <el-table-column label="功能名称" align="center" prop="forwardName" />
      <el-table-column label="转发编号" align="center" prop="forwardCode" />
      <el-table-column label="是否异步" align="center" prop="isAsync" :formatter="isAsyncFormat" />
      <el-table-column label="转发类型" align="center" prop="forwardType" :formatter="forwardTypeFormat" />
      <el-table-column label="转发协议" align="center" prop="forwardProtocol" :formatter="forwardProtocolFormat" />
      <el-table-column label="认证启用" align="center" prop="authEnabled" :formatter="authEnabledFormat" />
      <el-table-column label="认证类型" align="center" prop="authType" :formatter="authTypeFormat" />
      <el-table-column label="启用状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip=true />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['integrator:forwardInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['integrator:forwardInfo:remove']"
          >删除</el-button>
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

    <!-- 添加或修改转发配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-tabs tab-position="left" style="height: 450px;">
          <el-tab-pane label="基础配置">
            <el-form-item label="功能名称" prop="forwardName">
              <el-input v-model="form.forwardName" placeholder="请输入功能名称" />
            </el-form-item>
            <el-form-item label="转发编号" prop="forwardCode">
              <el-input v-model="form.forwardCode" placeholder="请输入转发编号" />
            </el-form-item>
            <el-form-item label="是否异步">
              <el-radio-group v-model="form.isAsync">
                <el-radio
                  v-for="dict in isAsyncOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="转发类型" prop="forwardType">
              <el-select v-model="form.forwardType" placeholder="请选择转发类型" @change="forwardTypeChange" >
                <el-option
                  v-for="dict in forwardTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="目标协议" prop="forwardProtocol">
              <el-select v-model="form.forwardProtocol" placeholder="请选择目标协议">
                <el-option
                  v-for="dict in forwardProtocolOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
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
          <el-tab-pane label="接口转发">
            <el-form-item label="转发URL" prop="forwardUrl">
              <el-input v-model="form.forwardUrl" placeholder="请输入转发URL，变量：${key}" :disabled="form_forwardUrl_disabled" />
            </el-form-item>
            <el-form-item label="方法类型" prop="forwardMethod">
              <el-select v-model="form.forwardMethod" placeholder="请选择方法类型" :disabled="form_forwardMethod_disabled" >
                <el-option
                  v-for="dict in forwardMethodOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="服务提供方" prop="authSource">
              <el-select v-model="form.authSource"  filterable >
                <el-option
                  v-for="item in appAccessOptions"
                  :key="item.appId"
                  :label="item.appKey"
                  :value="item.appId"
                >
                  <span style="float: left">{{ item.appKey }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.appEnCode }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="authEnabled">
              <span slot="label">
                <span>启用认证</span>
                <el-tooltip class="item" effect="dark" content="转发目标 url 的认证，即 http 头部 Authorization 信息" placement="top-start">
                  <i class="el-icon-question" style="color:darkblue;"></i>
                </el-tooltip>
              </span>
              <el-radio-group v-model="form.authEnabled">
                <el-radio
                  v-for="dict in authEnabledOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="认证类型" prop="authType">
              <el-select v-model="form.authType" placeholder="请选择认证类型">
                <el-option
                  v-for="dict in authTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>

          </el-tab-pane>
          <el-tab-pane label="SQL转发">
            <el-form-item label="SQL" prop="forwardSql">
              <el-input v-model="form.forwardSql" type="textarea" placeholder="请输入SQL内容，变量：${key}，动态参数：#{key}" :disabled="form_forwardSql_disabled" />
            </el-form-item>
            <el-form-item label="数据源" prop="forwardDatasource">
              <el-input v-model="form.forwardDatasource" placeholder="请输入数据源" :disabled="form_forwardDatasource_disabled" />
            </el-form-item>
            <el-form-item label="变量" prop="forwardVar" v-show="false" >
              <el-input v-model="form.forwardVar" placeholder="请输入变量，格式：{ 报文键 : 变量名 }" />
            </el-form-item>
            <el-form-item label="预编译STMT">
              <el-radio-group v-model="form.isPreparedStatement">
                <el-radio
                  v-for="dict in isPreparedStatementOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
        <!--
        <el-form-item label="自定义成功码" prop="rtStatusOk">
          <el-input v-model="form.rtStatusOk" placeholder="请输入自定义成功码" />
        </el-form-item>
        <el-form-item label="自定义失败码" prop="rtStatusError">
          <el-input v-model="form.rtStatusError" placeholder="请输入自定义失败码" />
        </el-form-item> -->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listForwardInfo, getForwardInfo, delForwardInfo, addForwardInfo, updateForwardInfo, exportForwardInfo } from "@/api/integrator/forwardInfo";
import { listAppAccess, getAppAccess, delAppAccess, addAppAccess, updateAppAccess, exportAppAccess } from "@/api/integrator/appAccess";

export default {
  name: "ForwardInfo",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      codes:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 转发配置表格数据
      forwardInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否异步字典
      isAsyncOptions: [],
      // 转发协议字典
      forwardProtocolOptions: [],
      // 转发类型字典
      forwardTypeOptions: [],
      // 方法类型字典
      forwardMethodOptions: [],
      // 预编译STMT字典
      isPreparedStatementOptions: [],
      // 认证启用字典
      authEnabledOptions: [],
      // 认证类型字典
      authTypeOptions: [],
      // 启用状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        forwardName: null,
        forwardCode: null,
        forwardProtocol: null,
        forwardType: null,
        forwardUrl: null,
        forwardSql: null,
        forwardDatasource: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        forwardName: [
          { required: true, message: "功能名称不能为空", trigger: "blur" }
        ],
        forwardCode: [
          { required: true, message: "转发编号不能为空", trigger: "blur" }
        ],
        isAsync: [
          { required: true, message: "是否异步不能为空", trigger: "blur" }
        ],
        forwardType: [
          { required: true, message: "转发类型不能为空", trigger: "change" }
        ],
        forwardProtocol: [
          { required: true, message: "转发协议不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "启用状态不能为空", trigger: "blur" }
        ],

      },
      appAccessOptions: [],
      form_forwardSql_disabled : false,
      form_forwardDatasource_disabled : false,
      form_forwardUrl_disabled: false,
      form_forwardMethod_disabled: false,
    };
  },
  created() {
    this.getList();
    this.getAppAccessList();
    this.getDicts("sys_yes_no").then(response => {
      this.isAsyncOptions = response.data;
    });
    this.getDicts("in_forward_protocol").then(response => {
      this.forwardProtocolOptions = response.data;
    });
    this.getDicts("in_forward_type").then(response => {
      this.forwardTypeOptions = response.data;
    });
    this.getDicts("in_forward_method_type").then(response => {
      this.forwardMethodOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.isPreparedStatementOptions = response.data;
    });
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.authEnabledOptions = response.data;
    });
    this.getDicts("in_http_authentication_type").then(response => {
      this.authTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询应用列表 */
    getAppAccessList() {
      listAppAccess().then(response => {
        this.appAccessOptions = response.rows;
      });
    },
    /** 查询转发配置列表 */
    getList() {
      this.loading = true;
      listForwardInfo(this.queryParams).then(response => {
        this.forwardInfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 是否异步字典翻译
    isAsyncFormat(row, column) {
      return this.selectDictLabel(this.isAsyncOptions, row.isAsync);
    },
    // 转发协议字典翻译
    forwardProtocolFormat(row, column) {
      return this.selectDictLabel(this.forwardProtocolOptions, row.forwardProtocol);
    },
    // 转发类型字典翻译
    forwardTypeFormat(row, column) {
      return this.selectDictLabel(this.forwardTypeOptions, row.forwardType);
    },
    // 方法类型字典翻译
    forwardMethodFormat(row, column) {
      return this.selectDictLabel(this.forwardMethodOptions, row.forwardMethod);
    },
    // 预编译STMT字典翻译
    isPreparedStatementFormat(row, column) {
      return this.selectDictLabel(this.isPreparedStatementOptions, row.isPreparedStatement);
    },
    // 启用状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 认证启用字典翻译
    authEnabledFormat(row, column) {
      return this.selectDictLabel(this.authEnabledOptions, row.authEnabled);
    },
    // 认证类型字典翻译
    authTypeFormat(row, column) {
      return this.selectDictLabel(this.authTypeOptions, row.authType?row.authType:"");
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        infoId: null,
        forwardName: null,
        forwardCode: null,
        isAsync: "N",
        forwardProtocol: null,
        forwardType: null,
        forwardUrl: null,
        forwardMethod: null,
        forwardSql: null,
        forwardDatasource: null,
        forwardVar: null,
        authEnabled: "N",
        authType: null,
        authSource: null,
        isPreparedStatement: "N",
        rtStatusOk: null,
        rtStatusError: null,
        status: "0",
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
      this.form_forwardSql_disabled = true;
      this.form_forwardDatasource_disabled = true;
      this.form_forwardUrl_disabled = false;
      this.form_forwardMethod_disabled = false;
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
      this.ids = selection.map(item => item.infoId)
      this.codes = selection.map(item => item.forwardCode)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加转发配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const infoId = row.infoId || this.ids
      getForwardInfo(infoId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改转发配置";
        this.controlDisable(this.form.forwardType);
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.infoId != null) {
            updateForwardInfo(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addForwardInfo(this.form).then(response => {
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
      const infoIds = row.infoId || this.ids;
      const forwardCodes = row.forwardCode || this.codes;
      this.$confirm('是否确认删除转发配置编号为"' + forwardCodes + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delForwardInfo(infoIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有转发配置数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportForwardInfo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    /**转发类型改变**/
    forwardTypeChange(item) {
      this.controlDisable(item);
    },

    controlDisable(forwardType){
      if (forwardType == "000000") {
        this.form_forwardSql_disabled = true;
        this.form_forwardDatasource_disabled = true;
        this.form_forwardUrl_disabled = false;
        this.form_forwardMethod_disabled = false;
      } else if (forwardType == "200001"){
        this.form_forwardSql_disabled = false;
        this.form_forwardDatasource_disabled = false;
        this.form_forwardUrl_disabled = true;
        this.form_forwardMethod_disabled = true;
      } else if (forwardType == "200011") {
        this.form_forwardSql_disabled = false;
        this.form_forwardDatasource_disabled = false;
        this.form_forwardUrl_disabled = true;
        this.form_forwardMethod_disabled = true;
      }
    }
  }
};
</script>
