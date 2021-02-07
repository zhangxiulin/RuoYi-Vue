<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用" prop="appId">
        <el-select v-model="queryParams.appId" size="small" filterable >
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
      <el-form-item label="转发" prop="fwdId">
        <el-select v-model="queryParams.fwdId" placeholder="转发" filterable clearable style="width:100%"  >
          <el-option
            v-for="item in forwardOptions"
            :key="item.infoId"
            :label="item.forwardCode"
            :value="item.infoId" >
            <span style="float: left">{{ item.forwardName }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.forwardCode }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
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
          v-hasPermi="['integrator:appAccessForward:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['integrator:appAccessForward:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['integrator:appAccessForward:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['integrator:appAccessForward:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appAccessForwardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="主键ID" align="center" prop="appFwdId" />-->
      <el-table-column label="应用" align="center" prop="appKey" />
      <el-table-column label="转发" align="center" prop="fwdCode" />
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['integrator:appAccessForward:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['integrator:appAccessForward:remove']"
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

    <!-- 添加或修改应用转发权限对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="应用" prop="appId" >
          <el-select v-model="queryParams.appId" :disabled="true" filterable style="width:100%" >
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
        <el-form-item label="转发" prop="fwdId">
          <el-select v-model="form.fwdId" placeholder="转发" filterable style="width:100%"  >
            <el-option
              v-for="item in forwardOptions"
              :key="item.infoId"
              :label="item.forwardCode"
              :value="item.infoId" >
              <span style="float: left">{{ item.forwardName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.forwardCode }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAppAccessForward, getAppAccessForward, delAppAccessForward, addAppAccessForward, updateAppAccessForward, exportAppAccessForward } from "@/api/integrator/appAccessForward";
import { listAppAccess, getAppAccess, delAppAccess, addAppAccess, updateAppAccess, exportAppAccess } from "@/api/integrator/appAccess";
import { listForwardInfo, getForwardInfo, delForwardInfo, addForwardInfo, updateForwardInfo, exportForwardInfo } from "@/api/integrator/forwardInfo";

export default {
  name: "AppAccessForward",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 应用转发权限表格数据
      appAccessForwardList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appId: null,
        fwdId: null,
        status: null,
      },
      // 默认应用id
      defaultAppId: '',
      defaultAppKey: '',
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      appAccessOptions: [],
      // 服务接口选项
      forwardOptions: [],
    };
  },
  created() {
    const appId = this.$route.params && this.$route.params.appId;
    this.getAppAccess(appId);
    this.getAppAccessList();
    this.getForwardInfoList();
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询应用列表 */
    getAppAccessList() {
      listAppAccess().then(response => {
        this.appAccessOptions = response.rows;
      });
    },
    /** 查询转发列表 */
    getForwardInfoList() {
      listForwardInfo().then(response => {
        this.forwardOptions = response.rows;
      });
    },
    /** 查询应用转发权限列表 */
    getList() {
      this.loading = true;
      listAppAccessForward(this.queryParams).then(response => {
        this.appAccessForwardList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        appFwdId: null,
        appId: null,
        fwdId: null,
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
      this.queryParams.appId = this.defaultAppId;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.appFwdId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.form.appId = this.queryParams.appId;
      this.form.appKey = this.defaultAppKey;
      this.title = "添加应用转发权限";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const appFwdId = row.appFwdId || this.ids
      getAppAccessForward(appFwdId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改应用转发权限";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.appFwdId != null) {
            updateAppAccessForward(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppAccessForward(this.form).then(response => {
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
      const appFwdIds = row.appFwdId || this.ids;
      this.$confirm('是否确认删除应用转发权限编号为"' + appFwdIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAppAccessForward(appFwdIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有应用转发权限数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportAppAccessForward(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    getAppAccess(appId) {
      getAppAccess(appId).then(response => {
        this.queryParams.appId = response.data.appId;
        this.defaultAppId = response.data.appId;
        this.defaultAppKey = response.data.appKey;
        this.getList();
      });
    }
  }
};
</script>
