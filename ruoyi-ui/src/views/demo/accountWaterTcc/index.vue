<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="72px">
      <el-form-item label="流水号" prop="serialNumber">
        <el-input
          v-model="queryParams.serialNumber"
          placeholder="请输入流水号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="卡号" prop="userCode">
        <el-input
          v-model="queryParams.userCode"
          placeholder="请输入卡号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="借贷标志" prop="jdFlag">
        <el-select v-model="queryParams.jdFlag" placeholder="请选择借贷标志" clearable size="small">
          <el-option
            v-for="dict in jdFlagOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="TCC阶段" prop="tccStage">
        <el-select v-model="queryParams.tccStage" placeholder="请选择TCC阶段" clearable size="small">
          <el-option
            v-for="dict in tccStageOptions"
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
      <!--<el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['demo:accountWaterTcc:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['demo:accountWaterTcc:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['demo:accountWaterTcc:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['demo:accountWaterTcc:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="accountWaterTccList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="主键ID" align="center" prop="id" />-->
      <el-table-column label="流水号" align="center" prop="serialNumber" />
      <el-table-column label="卡号" align="center" prop="userCode" />
      <el-table-column label="借贷标志" align="center" prop="jdFlag" :formatter="jdFlagFormat" />
      <el-table-column label="借金额" align="center" prop="jMoney" />
      <el-table-column label="贷金额" align="center" prop="dMoney" />
      <el-table-column label="TCC阶段" align="center" prop="tccStage" :formatter="tccStageFormat" />
      <el-table-column label="过期时间" align="center" prop="expires" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--<el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['demo:accountWaterTcc:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['demo:accountWaterTcc:remove']"
          >删除</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-user"
            @click="handleIntervene(scope.row)"
            v-hasPermi="['demo:accountWaterTcc:edit']"
          >人工干预</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['demo:accountWaterTcc:edit']"
          >详情</el-button>
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


    <!-- 查看TCC动账流水对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流水号" prop="serialNumber">
          <el-input v-model="form.serialNumber" placeholder="请输入流水号" :disabled="isView" />
        </el-form-item>
        <el-form-item label="卡号" prop="userCode">
          <el-input v-model="form.userCode" placeholder="请输入卡号" :disabled="isView" />
        </el-form-item>
        <el-form-item label="借贷标志">
          <el-radio-group v-model="form.jdFlag" :disabled="isView">
            <el-radio
              v-for="dict in jdFlagOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="借金额" prop="jMoney">
          <el-input v-model="form.jMoney" placeholder="请输入借金额" :disabled="isView" />
        </el-form-item>
        <el-form-item label="贷金额" prop="dMoney">
          <el-input v-model="form.dMoney" placeholder="请输入贷金额" :disabled="isView" />
        </el-form-item>
        <el-form-item label="TCC阶段" prop="tccStage">
          <el-select v-model="form.tccStage" placeholder="请选择TCC阶段"  :disabled="isView" >
            <el-option
              v-for="dict in tccStageOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="过期时间" prop="expires">
          <el-input v-model="form.expires" placeholder="请输入过期时间"  :disabled="isView" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注"  :disabled="isView" />
        </el-form-item>
      </el-form>
      <div v-if="!isView" slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <div v-else slot="footer" class="dialog-footer">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAccountWaterTcc, getAccountWaterTcc, delAccountWaterTcc, addAccountWaterTcc, updateAccountWaterTcc, exportAccountWaterTcc } from "@/api/demo/accountWaterTcc";

export default {
  name: "AccountWaterTcc",
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
      // TCC动账流水表格数据
      accountWaterTccList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 借贷标志字典
      jdFlagOptions: [],
      // TCC阶段字典
      tccStageOptions: [],
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serialNumber: null,
        userCode: null,
        jdFlag: null,
        tccStage: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        /*userCode: [
          { required: true, message: "卡号不能为空", trigger: "blur" }
        ],
        jdFlag: [
          { required: true, message: "借贷标志不能为空", trigger: "blur" }
        ],*/
      },
      isView: false
    };
  },
  created() {
    this.getList();
    this.getDicts("biz_jie_dai").then(response => {
      this.jdFlagOptions = response.data;
    });
    this.getDicts("dtx_tcc_stage").then(response => {
      this.tccStageOptions = response.data;
    });
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询TCC动账流水列表 */
    getList() {
      this.loading = true;
      listAccountWaterTcc(this.queryParams).then(response => {
        this.accountWaterTccList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 借贷标志字典翻译
    jdFlagFormat(row, column) {
      return this.selectDictLabel(this.jdFlagOptions, row.jdFlag);
    },
    // TCC阶段字典翻译
    tccStageFormat(row, column) {
      return this.selectDictLabel(this.tccStageOptions, row.tccStage);
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
        id: null,
        serialNumber: null,
        userCode: null,
        jdFlag: "J",
        jMoney: null,
        dMoney: null,
        tccStage: null,
        expires: null,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加TCC动账流水";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAccountWaterTcc(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改TCC动账流水";
      });
    },
    /** 修改按钮操作 */
    handleView(row) {
      this.reset();
      const id = row.id || this.ids
      getAccountWaterTcc(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.isView = true;
        this.title = "TCC动账流水详情";
      });
    },
    handleIntervene(row) {

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAccountWaterTcc(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAccountWaterTcc(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除TCC动账流水编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAccountWaterTcc(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有TCC动账流水数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportAccountWaterTcc(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
